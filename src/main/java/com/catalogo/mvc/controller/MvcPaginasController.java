package com.catalogo.mvc.controller;

import com.catalogo.mvc.model.ConceitoMVC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MvcPaginasController {

    private final List<ConceitoMVC> listaConceitos = new ArrayList<>();

    public MvcPaginasController() {
        listaConceitos.add(new ConceitoMVC(1, "Model", "Entidades Java", "Representam os dados e objetos do sistema.", "Backend"));
        listaConceitos.add(new ConceitoMVC(2, "View", "Thymeleaf", "Processa o HTML dinâmico no servidor.", "Frontend"));
        listaConceitos.add(new ConceitoMVC(3, "Controller", "Mapeamento HTTP", "Intercepta rotas e gerencia as requisições.", "Comunicação"));
    }

    // 🏠 Rota Inicial: http://localhost:8080/
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalConceitos", listaConceitos.size());
        return "inicio";
    }

    // 📦 Rota Model: http://localhost:8080/model
    @GetMapping("/model")
    public String paginaModel(Model model) {
        List<ConceitoMVC> apenasModel = listaConceitos.stream()
                .filter(c -> c.getComponente().equalsIgnoreCase("Model"))
                .collect(Collectors.toList());
        model.addAttribute("listaModel", apenasModel);
        return "model";
    }

    // 🖥️ Rota View: http://localhost:8080/view
    @GetMapping("/view")
    public String paginaView(Model model) {
        List<ConceitoMVC> apenasView = listaConceitos.stream()
                .filter(c -> c.getComponente().equalsIgnoreCase("View"))
                .collect(Collectors.toList());
        model.addAttribute("listaView", apenasView);
        return "view";
    }

    // ⚙️ Rota Controller: http://localhost:8080/controller
    @GetMapping("/controller")
    public String paginaController(Model model) {
        List<ConceitoMVC> apenasController = listaConceitos.stream()
                .filter(c -> c.getComponente().equalsIgnoreCase("Controller"))
                .collect(Collectors.toList());
        model.addAttribute("listaController", apenasController);
        return "controller";
    }

    // 🗄️ Rota Banco de Dados (Visão Geral): http://localhost:8080/db
    @GetMapping("/db")
    public String paginaDb(Model model) {
        model.addAttribute("todosConceitos", listaConceitos);
        return "db";
    }
}