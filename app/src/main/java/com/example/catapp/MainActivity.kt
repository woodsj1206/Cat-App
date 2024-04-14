/*
App Name: CatApp
Author: woodsj1206 (https://github.com/woodsj1206)
Description: A simple app that uses the CatAPI to display information about cats.
Course: CIS 436
Date Created: 4/10/24
Last Modified: 4/13/24
*/
package com.example.catapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.catapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}