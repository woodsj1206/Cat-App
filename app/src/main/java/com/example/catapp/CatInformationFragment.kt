package com.example.catapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.catapp.databinding.FragmentCatInformationBinding
import org.json.JSONArray
import org.json.JSONObject
import com.bumptech.glide.Glide

class CatInformationFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding : FragmentCatInformationBinding

    //Visit TheCatAPI (https://thecatapi.com/) to get an API key.
    private val apiKey = "REPLACE_WITH_YOUR_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCatInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedCat.observe(viewLifecycleOwner) { selectedCat ->
            // Change cat information
            if(sharedViewModel.currentCat.value != selectedCat){
                // Set cat information based on the selected item
                populateDisplay(selectedCat)

                // Fetch cat image
                fetchCatImage(selectedCat)

                // Update the current cat
                sharedViewModel.setCurrentCat(selectedCat)
            }
            else{
                populateDisplay(selectedCat)
            }
        }
    }

    private fun fetchCatImage(selectedItem: String) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://api.thecatapi.com/v1/images/search?api_key=${apiKey}&breed_id=${sharedViewModel.catBreeds.value?.get(selectedItem)?.getString("id")}"

        var imageUrl = ""
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val catImageArray: JSONArray = JSONArray(response)
                if (catImageArray.length() > 0) {
                    val catImage: JSONObject = catImageArray.getJSONObject(0)

                    if(catImage.has("url")){
                        imageUrl = catImage.getString("url")
                        Glide.with(requireContext()).load(imageUrl).into(binding.ivCatImage)
                        sharedViewModel.setCatImageUrl(imageUrl)
                    }
                }
            },
            { Log.e("CatApp", "FAILED TO GET RESPONSE") })

        queue.add(stringRequest)
    }

    private fun populateDisplay(selectedCat: String){
        // Set cat information based on the selected item
        binding.tvCatName.text = selectedCat

        // Retrieve the corresponding JSON object from catBreeds map
        val catBreeds = sharedViewModel.catBreeds.value
        val catInfo = catBreeds?.get(selectedCat)

        // Update UI elements with cat information
        catInfo?.let {
            val altName = if(it.has("alt_names")) it.getString("alt_names") else ""
            binding.tvCatAltName.text = if(altName.isEmpty()) "" else "Also Known As: $altName"

            binding.tvCatOrigin.text = if(it.has("origin")) it.getString("origin") else ""

            binding.tvCatTemperament.text = if(it.has("temperament")) it.getString("temperament") else ""

            binding.tvCatDescription.text = if(it.has("description")) it.getString("description") else ""
        }

        binding.ivCatImage.setImageResource(R.drawable.cat_app_placeholder)

        val imageUrl = sharedViewModel.catImageUrl.value

        if(!imageUrl.isNullOrEmpty()){
            Glide.with(requireContext()).load(imageUrl).into(binding.ivCatImage)
        }
    }
}