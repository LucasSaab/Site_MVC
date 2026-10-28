package com.empresa.gestaoti.controller;

import com.empresa.gestaoti.model.Projeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLLER (C do MVC)
 * Único controlador da aplicação. Faz três coisas:
 *   1. guarda a lista de projetos na memória RAM;
 *   2. recebe as requisições HTTP (rotas);
 *   3. escolhe qual View (arquivo HTML) será mostrada.
 */
@Controller
public class ProjetoController {

    // Logger do SLF4J: escreve mensagens no console a cada ação
    private static final Logger log = LoggerFactory.getLogger(ProjetoController.class);

    // Valores aceitos nos campos "status" e "prioridade" (alimentam os <select> do formulário)
    private static final String PLANEJADO = "Planejado";
    private static final String EM_ANDAMENTO = "Em andamento";
    private static final String CONCLUIDO = "Concluído";
    private static final List<String> STATUS_OPCOES = List.of(PLANEJADO, EM_ANDAMENTO, CONCLUIDO);
    private static final List<String> PRIORIDADE_OPCOES = List.of("Baixa", "Média", "Alta");

    // "Banco de dados" em memória RAM: some quando o servidor é reiniciado
    private final List<Projeto> projetos = new ArrayList<>();

    // Gerador simples de identificadores (1, 2, 3...)
    private long proximoId = 1;

    // Construtor: carrega alguns projetos de exemplo ao iniciar a aplicação
    public ProjetoController() {
        projetos.add(new Projeto(proximoId++, "Migração para Nuvem", "Ana Souza",
                "Mover os servidores internos para a nuvem pública.",
                EM_ANDAMENTO, "Alta", LocalDate.of(2026, 12, 15)));
        projetos.add(new Projeto(proximoId++, "Portal do Cliente v2", "Carlos Lima",
                "Redesenho do portal de autoatendimento.",
                PLANEJADO, "Média", LocalDate.of(2027, 3, 31)));
        projetos.add(new Projeto(proximoId++, "Implantação do ERP", "Mariana Costa",
                "Substituição do sistema financeiro legado.",
                CONCLUIDO, "Alta", LocalDate.of(2026, 8, 30)));
        projetos.add(new Projeto(proximoId++, "Pipeline CI/CD", "Rafael Alves",
                "Automatizar build, testes e deploy das aplicações.",
                EM_ANDAMENTO, "Média", LocalDate.of(2026, 11, 30)));
        projetos.add(new Projeto(proximoId++, "Política de Segurança", "Juliana Rocha",
                "Documentar e publicar as normas de segurança da informação.",
                CONCLUIDO, "Baixa", LocalDate.of(2026, 6, 20)));
        log.info("Aplicação iniciada com {} projetos de exemplo", projetos.size());
    }

    // ===================== LISTAR =====================

    // GET /  ->  redireciona para a lista
    @GetMapping("/")
    public String raiz() {
        return "redirect:/projetos";
    }

    // GET /projetos  ->  dashboard com KPIs e tabela
    @GetMapping("/projetos")
    public String listar(Model model) {
        // O objeto "model" é a bandeja que leva dados do Controller até a View
        model.addAttribute("projetos", projetos);
        model.addAttribute("totalProjetos", projetos.size());
        model.addAttribute("planejados", contarPorStatus(PLANEJADO));
        model.addAttribute("emAndamento", contarPorStatus(EM_ANDAMENTO));
        model.addAttribute("concluidos", contarPorStatus(CONCLUIDO));
        log.info("Listando {} projetos", projetos.size());
        return "projetos/lista";   // abre templates/projetos/lista.html
    }

    // ===================== CRIAR =====================

    // GET /projetos/novo  ->  mostra o formulário vazio
    @GetMapping("/projetos/novo")
    public String novo(Model model) {
        model.addAttribute("projeto", new Projeto());
        adicionarOpcoes(model);
        log.info("Abrindo formulário de novo projeto");
        return "projetos/form";
    }

    // POST /projetos/criar  ->  recebe o formulário e adiciona na lista
    // O Spring preenche o objeto "projeto" com os campos do formulário
    @PostMapping("/projetos/criar")
    public String criar(@ModelAttribute Projeto projeto, RedirectAttributes redirect) {
        projeto.setId(proximoId++);
        projetos.add(projeto);
        log.info("Projeto ID {} criado com sucesso: '{}'", projeto.getId(), projeto.getNome());
        redirect.addFlashAttribute("mensagem", "Projeto criado com sucesso!");
        return "redirect:/projetos";   // volta para a lista (padrão Post/Redirect/Get)
    }

    // ===================== EDITAR =====================

    // GET /projetos/{id}/editar  ->  mostra o formulário preenchido
    @GetMapping("/projetos/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<Projeto> encontrado = buscarPorId(id);
        if (encontrado.isEmpty()) {
            log.warn("Edição ignorada: projeto ID {} não encontrado", id);
            redirect.addFlashAttribute("mensagem", "Projeto não encontrado.");
            return "redirect:/projetos";
        }
        model.addAttribute("projeto", encontrado.get());
        adicionarOpcoes(model);
        log.info("Abrindo formulário de edição do projeto ID {}", id);
        return "projetos/form";
    }

    // POST /projetos/{id}/atualizar  ->  copia os dados do formulário para o projeto da lista
    @PostMapping("/projetos/{id}/atualizar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Projeto dados, RedirectAttributes redirect) {
        Optional<Projeto> encontrado = buscarPorId(id);
        if (encontrado.isEmpty()) {
            log.warn("Atualização ignorada: projeto ID {} não encontrado", id);
            redirect.addFlashAttribute("mensagem", "Projeto não encontrado.");
            return "redirect:/projetos";
        }
        Projeto projeto = encontrado.get();
        projeto.setNome(dados.getNome());
        projeto.setResponsavel(dados.getResponsavel());
        projeto.setDescricao(dados.getDescricao());
        projeto.setStatus(dados.getStatus());
        projeto.setPrioridade(dados.getPrioridade());
        projeto.setPrazo(dados.getPrazo());
        log.info("Projeto ID {} atualizado com sucesso", id);
        redirect.addFlashAttribute("mensagem", "Projeto atualizado com sucesso!");
        return "redirect:/projetos";
    }

    // ===================== EXCLUIR =====================

    // POST /projetos/{id}/excluir  ->  remove o projeto da lista
    @PostMapping("/projetos/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        boolean removido = projetos.removeIf(p -> p.getId().equals(id));
        if (removido) {
            log.info("Projeto ID {} excluído com sucesso", id);
            redirect.addFlashAttribute("mensagem", "Projeto excluído com sucesso!");
        } else {
            log.warn("Exclusão ignorada: projeto ID {} não encontrado", id);
            redirect.addFlashAttribute("mensagem", "Projeto não encontrado.");
        }
        return "redirect:/projetos";
    }

    // ===================== MÉTODOS AUXILIARES =====================

    // Procura um projeto na lista pelo ID
    private Optional<Projeto> buscarPorId(Long id) {
        return projetos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // Conta quantos projetos têm determinado status (usado nos cards de KPI)
    private long contarPorStatus(String status) {
        return projetos.stream()
                .filter(p -> status.equals(p.getStatus()))
                .count();
    }

    // Envia as opções dos <select> para o formulário
    private void adicionarOpcoes(Model model) {
        model.addAttribute("statusOpcoes", STATUS_OPCOES);
        model.addAttribute("prioridadeOpcoes", PRIORIDADE_OPCOES);
    }
}
