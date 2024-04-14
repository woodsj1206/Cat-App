package com.example.catapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.catapp.databinding.FragmentCatSelectionBinding
import org.json.JSONArray
import org.json.JSONObject

class CatSelectionFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding : FragmentCatSelectionBinding

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
        binding = FragmentCatSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner: Spinner = binding.spCatSelector

        // Check if cat breeds data is already available
        val catBreeds = sharedViewModel.catBreeds.value
        if (catBreeds != null) {
            // Populate spinner with existing data
            populateSpinner(catBreeds)
        } else {
            // Fetch cat breeds and populate spinner when done
            getCatBreeds { catMap ->
                // Store cat breeds data in ViewModel
                sharedViewModel.setCatBreeds(catMap)
                // Populate spinner
                populateSpinner(catMap)
            }
        }

        // Set a listener to handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0){
                    val selectedCat = spinner.selectedItem.toString()

                    if(sharedViewModel.currentCat.value != selectedCat){
                        // Notify SharedViewModel about the selected item
                        sharedViewModel.setSelectedCat(selectedCat)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle no selection if needed
            }
        }
    }

    private fun getCatBreeds(callback: (Map<String, JSONObject>) -> Unit) {
        val catMap = mutableMapOf<String, JSONObject>()

        val queue = Volley.newRequestQueue(context)
        val url = "https://api.thecatapi.com/v1/breeds?api_key=${apiKey}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {response ->
                val catsArray : JSONArray = JSONArray(response)

                // Add each cat json objects to the catMap
                for(i in 0 until catsArray.length()) {
                    val theCat : JSONObject = catsArray.getJSONObject(i)

                    if(theCat.has("name")){
                        catMap[theCat.getString("name")] = theCat
                    }
                }

                // Call the callback with the populated catMap
                callback(catMap)
            },
            { Log.e("CatApp", "FAILED TO GET A RESPONSE") })

        queue.add(stringRequest)
    }

    private fun populateSpinner(catMap: Map<String, JSONObject>) {
        val selectionList = listOf<String>("Select a Cat...") + catMap.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, selectionList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCatSelector.adapter = adapter
    }
}