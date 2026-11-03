(function () {
  'use strict';

  var CHAVE_MODO = 'mvc-modo-apresentacao';

  /* =====================================================
     1. Modo apresentação (fonte maior e contraste reforçado)
     A classe também é aplicada no <head> para evitar "piscar".
     ===================================================== */
  var botaoModo = document.getElementById('modo-apresentacao');
  if (botaoModo) {
    var raiz = document.documentElement;
    var sincronizarModo = function () {
      botaoModo.setAttribute('aria-pressed', raiz.classList.contains('modo-apresentacao') ? 'true' : 'false');
    };
    sincronizarModo();
    botaoModo.addEventListener('click', function () {
      var ligado = raiz.classList.toggle('modo-apresentacao');
      try { localStorage.setItem(CHAVE_MODO, ligado ? '1' : '0'); } catch (e) { /* armazenamento indisponível */ }
      sincronizarModo();
    });
  }

  /* =====================================================
     2. Botão "Copiar" nos blocos de código (terminal)
     ===================================================== */
  var copiarTexto = function (texto) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      return navigator.clipboard.writeText(texto);
    }
    return new Promise(function (resolve, reject) {
      var area = document.createElement('textarea');
      area.value = texto;
      area.setAttribute('readonly', '');
      area.style.position = 'fixed';
      area.style.opacity = '0';
      document.body.appendChild(area);
      area.select();
      var ok = false;
      try { ok = document.execCommand('copy'); } catch (e) { ok = false; }
      document.body.removeChild(area);
      if (ok) { resolve(); } else { reject(new Error('cópia não suportada')); }
    });
  };

  document.querySelectorAll('.terminal').forEach(function (figura) {
    var legenda = figura.querySelector('figcaption');
    var pre = figura.querySelector('pre');
    if (!legenda || !pre) { return; }

    var botao = document.createElement('button');
    botao.type = 'button';
    botao.className = 'copiar';
    botao.textContent = 'Copiar';
    botao.setAttribute('aria-label', 'Copiar o código de ' + legenda.textContent.trim());
    legenda.appendChild(botao);

    botao.addEventListener('click', function () {
      var texto = pre.textContent.replace(/\u200b/g, '');
      copiarTexto(texto).then(function () {
        botao.textContent = 'Copiado';
      }, function () {
        botao.textContent = 'Falhou';
      }).then(function () {
        setTimeout(function () { botao.textContent = 'Copiar'; }, 1600);
      });
    });
  });

  /* =====================================================
     3. Índice lateral: destaca a seção que está na tela
     ===================================================== */
  var linksIndice = Array.prototype.slice.call(document.querySelectorAll('.toc a[href^="#"]'));
  if (linksIndice.length) {
    var alvos = linksIndice.map(function (link) {
      return document.getElementById(decodeURIComponent(link.getAttribute('href').slice(1)));
    });
    var agendado = false;

    var atualizarIndice = function () {
      agendado = false;
      var atual = 0;
      alvos.forEach(function (alvo, i) {
        if (alvo && alvo.getBoundingClientRect().top <= 140) { atual = i; }
      });
      // No fim da página, a última seção pode não alcançar o limite
      var noFim = window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 4;
      if (noFim && window.scrollY > 0) { atual = linksIndice.length - 1; }

      linksIndice.forEach(function (link, i) {
        if (i === atual) { link.setAttribute('aria-current', 'true'); }
        else { link.removeAttribute('aria-current'); }
      });
    };
    var agendar = function () {
      if (agendado) { return; }
      agendado = true;
      (window.requestAnimationFrame || window.setTimeout)(atualizarIndice);
    };

    window.addEventListener('scroll', agendar, { passive: true });
    window.addEventListener('resize', agendar);
    atualizarIndice();
  }

  /* =====================================================
     4. Seções de aprofundamento: abrem ao navegar até elas
     ===================================================== */
  var abrirSecaoDoAlvo = function (hash) {
    if (!hash || hash.length < 2) { return; }
    var alvo = document.getElementById(decodeURIComponent(hash.slice(1)));
    var secao = alvo && alvo.closest ? alvo.closest('details') : null;
    if (secao && !secao.open) { secao.open = true; }
  };
  document.addEventListener('click', function (evento) {
    var link = evento.target.closest ? evento.target.closest('a[href^="#"]') : null;
    if (link) { abrirSecaoDoAlvo(link.getAttribute('href')); }
  });
  window.addEventListener('hashchange', function () { abrirSecaoDoAlvo(window.location.hash); });
  abrirSecaoDoAlvo(window.location.hash);

  /* =====================================================
     5. Filtro por componente (db.html)
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
