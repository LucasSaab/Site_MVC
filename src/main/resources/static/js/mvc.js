(function () {
  'use strict';

  /* =====================================================
     1. Etapas do ciclo da requisição (inicio.html)
     Melhoria progressiva: sem JavaScript, os cartões
     aparecem todos em sequência.
     ===================================================== */
  var container = document.getElementById('etapas');
  if (container) {
    var cartoes = container.querySelectorAll('.etapa-cartao');
    var lista = container.querySelector('.etapas-lista');
    var barra = container.querySelector('.etapas-progresso span');
    var anterior = document.getElementById('etapa-anterior');
    var proxima = document.getElementById('etapa-proxima');
    var pontos = document.getElementById('etapa-pontos');
    var total = cartoes.length;
    var atual = 0;
    var botoesPonto = [];

    container.classList.add('etapas-ativo');
    lista.setAttribute('aria-live', 'polite');

    // Indicadores numerados (1 a N)
    cartoes.forEach(function (_, i) {
      var item = document.createElement('li');
      var botao = document.createElement('button');
      botao.type = 'button';
      botao.className = 'ponto';
      botao.textContent = String(i + 1);
      botao.setAttribute('aria-label', 'Etapa ' + (i + 1) + ' de ' + total);
      botao.addEventListener('click', function () { irPara(i); });
      item.appendChild(botao);
      pontos.appendChild(item);
      botoesPonto.push(botao);
    });

    var irPara = function (i) {
      atual = Math.max(0, Math.min(total - 1, i));

      cartoes.forEach(function (cartao, idx) { cartao.hidden = idx !== atual; });

      botoesPonto.forEach(function (botao, idx) {
        botao.classList.toggle('feito', idx < atual);
        if (idx === atual) { botao.setAttribute('aria-current', 'step'); }
        else { botao.removeAttribute('aria-current'); }
      });

      barra.style.width = ((atual + 1) / total * 100) + '%';
      anterior.disabled = atual === 0;
      proxima.disabled = atual === total - 1;
      proxima.textContent = atual === total - 1 ? 'Ciclo concluído' : 'Próxima →';
    };

    anterior.addEventListener('click', function () { irPara(atual - 1); });
    proxima.addEventListener('click', function () { irPara(atual + 1); });

    // Navegação por teclado dentro do container
    container.addEventListener('keydown', function (e) {
      if (e.key === 'ArrowRight') { irPara(atual + 1); e.preventDefault(); }
      if (e.key === 'ArrowLeft') { irPara(atual - 1); e.preventDefault(); }
    });

    irPara(0);
  }

  /* =====================================================
     2. Filtro por componente (db.html)
     ===================================================== */
  var filtros = document.querySelectorAll('.filtro[data-filtro]');
  if (filtros.length) {
    var linhas = document.querySelectorAll('tr[data-componente]');
    var contador = document.getElementById('db-contador');

    filtros.forEach(function (botao) {
      botao.addEventListener('click', function () {
        var alvo = botao.getAttribute('data-filtro');
        var visiveis = 0;

        filtros.forEach(function (f) {
          f.setAttribute('aria-pressed', f === botao ? 'true' : 'false');
        });

        linhas.forEach(function (linha) {
          var mostrar = alvo === 'todos' || linha.getAttribute('data-componente') === alvo;
          linha.hidden = !mostrar;
          if (mostrar) { visiveis++; }
        });

        if (contador) { contador.textContent = visiveis; }
      });
    });
  }
})();
