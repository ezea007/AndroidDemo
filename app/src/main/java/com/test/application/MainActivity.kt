package com.test.application

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.test.application.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //下面代码的 意思是：当切换对应的Fragment时，动态修改ActionBar标题文字，
        //目的为了让运行效果明显
        navController = Navigation.findNavController(this, R.id.fragment1)

        //监听页面切换
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("hqk", "onDestinationChanged")
        }


//        NavigationUI.setupActionBarWithNavController(this, navController)

        //底部菜单
        NavigationUI.setupWithNavController(activityMainBinding.bottom, navController!!)
    }

    //设置Action 菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    //Action Bar菜单选项选中时
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController!!) || super.onOptionsItemSelected(item)
    }

}