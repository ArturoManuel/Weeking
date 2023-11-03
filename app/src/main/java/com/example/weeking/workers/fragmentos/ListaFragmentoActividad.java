package com.example.weeking.workers.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.ActividadesAdapter;
import com.example.weeking.Adapter.EventosAdapter;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.ActividadActivity;
import com.example.weeking.workers.ActividadesActivity;
import com.example.weeking.workers.viewModels.AppViewModel;

import java.util.ArrayList;
import java.util.List;


public class ListaFragmentoActividad extends Fragment implements ActividadesAdapter.OnActividadClickListener {

    private RecyclerView recyclerView;
    private ActividadesAdapter adapter;

    // Añadir instancia del ViewModel
    private AppViewModel appViewModel;

    public ListaFragmentoActividad() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener la instancia del ViewModel
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_fragmento_actividad, container, false);

        // Configuración inicial del RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewActividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Observar cambios en la lista de actividades del ViewModel
        appViewModel.listaActividades.observe(getViewLifecycleOwner(), actividadList -> {
            if (actividadList != null) {
                adapter = new ActividadesAdapter(actividadList, getActivity());
                adapter.setOnActividadClickListener(this);  // Establecer el listener aquí
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onActividadClick(Actividad actividad) {
        abrirDetalleActividad(actividad);
    }

    private void abrirDetalleActividad(Actividad actividad) {
        // Aquí, no sé de dónde proviene DataHolder y mListener. Si son parte de tu implementación original,
        // puedes dejar este método como estaba.
        DataHolder.getInstance().setActividadSeleccionada(actividad);
        //mListener.onActividadSelected(actividad);  // Si no usas mListener, puedes comentar o quitar esta línea

        Intent intent = new Intent(getActivity(), ActividadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEliminarActividadClick(int position) {
        // Obtiene la actividad actual para eliminar
        Actividad actividadParaEliminar = appViewModel.listaActividades.getValue().get(position);


        // Llama al método en tu ViewModel para eliminar la actividad
        appViewModel.eliminarActividad(actividadParaEliminar);

        // No es necesario actualizar la lista aquí,
        // ya que el observer de 'listaActividades' se encargará de actualizar el RecyclerView automáticamente.
    }


}


