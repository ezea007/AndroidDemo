package com.test.application

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import org.junit.Test

class KotlinTesst {
    @Test
    fun `test know channel`() = runBlocking<Unit> {
        val channel = Channel<Int>(Channel.UNLIMITED)
        //生产者
        val producer = GlobalScope.launch {
            var i = 0
            for (x in 1..5) {
                channel.send(x)
                println("send ${x}")
            }
        }

        //消费者
        val consumer = GlobalScope.launch {
            while (true) {
                delay(1000)
                val element = channel.receive()
                println("receive $element")
            }
        }
        joinAll(producer, consumer)
    }

    @Test
    fun `test fast producer channel`() = runBlocking<Unit> {
        //生产者，
        val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce<Int> {
            repeat(100) {
                delay(1000)
                send(it)
            }
        }
        //消费者
        val consumer = GlobalScope.launch {
            for (i in receiveChannel) {
                println("received: $i")
            }
        }
        consumer.join()
    }

    @Test
    fun `test fast consumer channel`() = runBlocking<Unit> {
        val sendChannel: SendChannel<Int> = GlobalScope.actor<Int> {
            while (true) {
                val element = receive()
                println(element)
            }
        }

        val producer = GlobalScope.launch {
            for (i in 0..3) {
                sendChannel.send(i)
            }
        }

        producer.join()
    }

    @Test
    fun `test close channel`() = runBlocking {
        val channel = Channel<Int>(3)
        //生产者
        val producer = GlobalScope.launch {
            List(3) {
                channel.send(it)
                println("send $it")
            }
            //由生产者主导生命周期，执行关闭！
            channel.close()
            println(
                """close channel. 
                |  - ClosedForSend: ${channel.isClosedForSend}
                |  - ClosedForReceive: ${channel.isClosedForReceive}""".trimMargin()
            )
        }

        //消费者
        val consumer = GlobalScope.launch {
            for (element in channel) {
                delay(1000)
                println("receive $element")
            }
            println(
                """After Consuming. 
                |   - ClosedForSend: ${channel.isClosedForSend} 
                |   - ClosedForReceive: ${channel.isClosedForReceive}""".trimMargin()
            )
        }

        joinAll(producer, consumer)
    }

    @Test
    fun `test broadcast`() = runBlocking<Unit> {
        //val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
        val channel = Channel<Int>() //这里使用默认缓存区大小
        //初始化三个消费者
        val broadcastChannel = channel.broadcast(3)
        val producer = GlobalScope.launch {
            List(3) {
                delay(100)
                broadcastChannel.send(it)
            }
            //由主导方管理生命周期
            broadcastChannel.close()
        }

        //创建三个消费者
        List(3) { index ->
            GlobalScope.launch {
                val receiveChannel = broadcastChannel.openSubscription()
                for (i in receiveChannel) {
                    println("[#$index] received: $i")
                }
            }
        }.joinAll()
    }

    @Test
    fun `test select channel`() = runBlocking<Unit> {
        val channels = listOf(Channel<Int>(), Channel<Int>())
        GlobalScope.launch {
            delay(100)
            channels[0].send(200)
        }

        GlobalScope.launch {
            delay(50)
            channels[1].send(100)
        }

        val result = select<Int?> {
            channels.forEach { channel ->
                channel.onReceive { it }
            }
        }
        println(result)
    }

    @Test
    fun `test SelectClause0`() = runBlocking<Unit> {
        val job1 = GlobalScope.launch {
            delay(100)
            println("job 1")
        }

        val job2 = GlobalScope.launch {
            delay(10)
            println("job 2")
        }

        select<Unit> {
            job1.onJoin { println("job 1 onJoin") }
            job2.onJoin { println("job 2 onJoin") }
        }
        delay(1000)
    }

    @Test
    fun `test SelectClause2`() = runBlocking<Unit> {
        val channels = listOf(Channel<Int>(), Channel<Int>())
        println(channels)
        launch(Dispatchers.IO) {
            select<Unit?> {
                launch {
                    delay(10)
                    channels[1].onSend(200) { sentChannel ->
                        println("sent 1 on $sentChannel")
                    }
                }
                launch {
                    delay(100)
                    channels[0].onSend(100) { sentChannel ->
                        println("sent 0 on $sentChannel")
                    }
                }
            }
        }
        GlobalScope.launch {
            println(channels[0].receive())
        }
        GlobalScope.launch {
            println(channels[1].receive())
        }
        delay(1000)
    }

    @Test
    fun `test not safe concurrent`() = runBlocking<Unit> {
        var count = 0
        List(1000) {
            GlobalScope.launch { count++ }
        }.joinAll()
        println(count)
    }

    @Test
    fun `test safe concurrent tools`() = runBlocking<Unit> {
        var count = 0
        val mutex = Mutex()
        List(1000) {
            GlobalScope.launch {
                mutex.withLock {
                    count++
                }
            }
        }.joinAll()
        println(count)
    }

    @Test
    fun `test safe concurrent tools2`() = runBlocking<Unit> {
        var count = 0
        val semaphore = Semaphore(1)
        List(1000) {
            GlobalScope.launch {
                semaphore.withPermit {
                    count++
                }
            }
        }.joinAll()
        println(count)
    }

    @Test
    fun `test avoid access outer variable`() = runBlocking<Unit> {
        var count = 0
        val result = count + List(1000) {
            GlobalScope.async { 1 }
        }.map { it.await() }.sum()
        println(result)
    }

    fun simpleFlow2() = flow<Int> {
        println("Flow started")
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }

    @Test
    fun `test flow is cold`() = runBlocking<Unit> {
        val flow = simpleFlow2()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    @Test
    fun `test terminal operator`() = runBlocking<Unit> {
        val sum = (1..5).asFlow()
            .map {
                println("it * it= ${it * it}")
                it * it
            }
            //从第一个元素开始累加值，并将操作应用于当前累加器值和每个元素
            .reduce { a, b ->
                println("a=$a,b=$b,a+b=${a + b}")
                a + b
            }
        println(sum)
    }

    @Test
    fun `test zip2`() = runBlocking<Unit> {
        val numbs = (1..3).asFlow().onEach { delay(300) }
        val strs = flowOf("One", "Two", "Three").onEach { delay(400) }
        val startTime = System.currentTimeMillis()
        numbs.zip(strs) { a, b -> "$a -> $b" }.collect {
            println("$it at ${System.currentTimeMillis() - startTime} ms from start")
        }
    }

    private fun requestFlow(i: Int) = flow<String> {
        emit("$i: First")
        delay(1000)
        emit("$i: Second")
    }

    @Test
    fun `test flatMapConcat`() = runBlocking<Unit> {
        //Flow<Flow<String>>
        val startTime = System.currentTimeMillis()
        (1..3).asFlow()
            .onEach { delay(100) }
            //.map { requestFlow(it) }
            .flatMapMerge { requestFlow(it) }
            .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
    }


}