package com.derekgd.curso.tesis.clases

import android.content.pm.PackageManager

class AppStatus {

    fun isAppInstalled(packageName:String,packageManager: PackageManager):Boolean{
        return try {
            packageManager.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES)
            true
        }catch (e:PackageManager.NameNotFoundException){
            false
        }


    }


}