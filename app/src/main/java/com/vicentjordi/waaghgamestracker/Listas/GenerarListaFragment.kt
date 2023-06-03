package com.vicentjordi.waaghgamestracker.Listas

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.FaccionesFragment
import com.vicentjordi.waaghgamestracker.databinding.FragmentGenerarListaBinding


class GenerarListaFragment : Fragment(), FaccionesFragment.seleccionarFaccion {
    private var callback: OnBackPressedCallback? = null
    private var _binding: FragmentGenerarListaBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private var faccion: String? = null
    private var idLista: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGenerarListaBinding.inflate(inflater, container, false)

        tipoLista()

        binding.faccion.setOnClickListener {
            val fragmentFaccion = FaccionesFragment()
            fragmentFaccion.setFaccionClickListener(this@GenerarListaFragment)
            cargarFragments(fragmentFaccion)
        }

        binding.btnBuscar.setOnClickListener {
            if (faccion != null){
                if (idLista != null)
                buscarMini()
                else
                    crearLista()
            }
            else
                sinfaccion()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.recyclerView.adapter?.itemCount ?: 0 > 1) {
                    mostrarDialogoSalir()
                } else {
                    // Si no hay más de 1 elemento en el RecyclerView, simplemente se maneja el evento de retroceso normalmente.
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun tipoLista(){
        val itemsLista = listOf("Incursion(1000 puntos)", "Strike Force(2000 puntos)",
                "Onslaught(3000 puntos)", "Patrulla de Combate(500 puntos)")
        val spinner: Spinner = binding.spinnerFacciones
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemsLista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorListasFragments, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun crearLista(){
        val context = requireContext()
        val prefs = context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", "") ?: ""
        val listaMini: List<DocumentReference> = emptyList()

        val listaData = hashMapOf<String, Any>(
            "email" to email,
            "nombreLista" to binding.txtNombreLista.text.toString(),
            "faccion" to faccion as Any,
            "tipoLista" to binding.spinnerFacciones.selectedItem.toString(),
            "listaMini" to listaMini
        )

        db.collection("listas")
            .add(listaData)
            .addOnSuccessListener { documentReference ->
                idLista = documentReference.id
                val idLista = documentReference.id
                onListaCreada(idLista)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "No se ha podido crear la lista", Toast.LENGTH_SHORT).show()
            }
    }
     fun onListaCreada(idLista: String) {
        this.idLista = idLista
        buscarMini()
    }
    private fun buscarMini() {
        val bundle = Bundle()
        val fragment = BuscarMiniFragment()

        bundle.putString("faccion", faccion)
        bundle.putString("idLista", idLista)

        Toast.makeText(requireContext(), "$idLista", Toast.LENGTH_SHORT).show()
        fragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorListasFragments, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun devolverFaccion(faccionSeleccionada: String) {
        //Asegura que se realize la actualizacion del textView en el hilo Principal.
        requireActivity().runOnUiThread{
            faccion = faccionSeleccionada
        }
    }

    override fun onResume() {
        super.onResume()
        if (faccion == null)
            binding.faccion.text = getText(R.string.faccion)
        else
            binding.faccion.text = faccion
    }

    private fun mostrarDialogoSalir(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Salir")
            .setMessage("¿Estás seguro de que quieres salir? Se borrará todo el progreso.")
            .setPositiveButton("Aceptar") { dialog, which ->
                // Realizar las acciones necesarias antes de salir
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun sinfaccion(){
        Toast.makeText(requireContext(),
            R.string.sinFaccion,
            Toast.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        callback?.remove() // Remover el callback al destruir la vista
        _binding = null
    }
}