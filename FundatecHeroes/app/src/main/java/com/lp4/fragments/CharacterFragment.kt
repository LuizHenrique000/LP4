package com.lp4.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lp4.R
import com.lp4.adapter.PersonagemListAdapter
import com.lp4.api.PersonagemClient
import com.lp4.api.UsuarioClient
import com.lp4.model.UsuarioResponse
import com.lp4.databinding.FragmentHeroiBinding
import com.lp4.domain.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var fragmentHeroiBinding: FragmentHeroiBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE // torna a ProgressBar visível

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonUsuarioResponse = sharedPreferences.getString("user", "")
            val usuarioResponse = gson.fromJson(jsonUsuarioResponse, UsuarioResponse::class.java)
            val apiClient = PersonagemClient()
            val id = usuarioResponse.id.toString()
            val personagens = apiClient.getPersonagens(id)

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                val recyclerView: RecyclerView = fragmentHeroiBinding.userList
                val adapter = PersonagemListAdapter()
                recyclerView.adapter = adapter
                if (personagens != null) {
                    adapter.setItems(personagens)
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHeroiBinding.inflate(inflater, container, false)
        fragmentHeroiBinding = binding
        return binding.root
    }

    companion object {

        lateinit var categoryType: CategoryType

        fun newInstance(type: CategoryType): Fragment {
            categoryType = type
            return CharacterFragment()
        }

    }
}
