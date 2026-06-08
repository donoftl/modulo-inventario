package com.prueba.tecnica.Sof.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.tecnica.Sof.dto.repository.CategoriaRepository;
import com.prueba.tecnica.Sof.model.Categoria;
import com.prueba.tecnica.Sof.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true) // Optimiza la consulta en modo lectura para la Base de Datos
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}