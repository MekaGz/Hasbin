package com.ramos.myapplication

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object PermissionTracking {

    fun hasContactPermissions(context: Context):Boolean = EasyPermissions.hasPermissions(
        context,
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS,
    )
}