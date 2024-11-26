package com.ramos.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramos.myapplication.databinding.FragmentHasbinearContactosBinding
import com.vmadalin.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AppSettingsDialog


class hasbinearContactos : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentHasbinearContactosBinding
    var arrayList:ArrayList<ModeloContacto> = arrayListOf()
    var rcAdapter:RCAdapter = RCAdapter(this, arrayList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHasbinearContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        var prueba = sharedPreferences.getString("uuid", null).toString()

        Log.d("prueba uuid", prueba)

        if(checkContactPermissions()){
            binding.apply {
                rContactos.apply {
                    layoutManager = LinearLayoutManager(this@hasbinearContactos)
                    adapter = rcAdapter
                }
            }

            getContact()
        }

        binding.btnX2.setOnClickListener {
            finish()
        }
    }

    private fun getContact(){
        arrayList.clear()
        val cursor =  this.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                ), null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

        while(cursor!!.moveToNext()){
            val nombreContacto = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val numeroContacto = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val objModeloContacto = ModeloContacto(nombreContacto, numeroContacto)
            arrayList.add(objModeloContacto)
        }

        rcAdapter.notifyDataSetChanged()
        cursor.close()
    }

    private fun checkContactPermissions():Boolean{
        if(PermissionTracking.hasContactPermissions(this)){
            return true
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            EasyPermissions.requestPermissions(
                this,
                "Necesitas otorgar permisos para acceder a esta funcion.",
                100,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
            )
            return true

        }
        else{
            return false
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            checkContactPermissions()
        }
    }

}