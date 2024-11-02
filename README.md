# 🕵️‍♂️ Ladrão vs Poupador 🏦

Este projeto é uma simulação de um agente inteligente, o **Poupador** 💰, que precisa sobreviver e coletar moedas em um ambiente cheio de **Ladrões** 🦹‍♂️. O Poupador utiliza técnicas de IA para decidir seus movimentos e maximizar sua coleta de moedas enquanto evita os Ladrões.

## 📋 Índice

- [📜 Descrição do Projeto](#descrição-do-projeto)
- [🗂️ Estrutura do Projeto](#estrutura-do-projeto)
- [📐 Regras do Jogo](#regras-do-jogo)
- [🧠 Estratégias do Poupador](#estratégias-do-poupador)
- [⚙️ Configuração do Ambiente](#configuração-do-ambiente)
- [🚀 Como Executar](#como-executar)
- [🤝 Contribuições](#contribuições)

## 📜 Descrição do Projeto

O projeto **Ladrão vs Poupador** é uma simulação que combina algoritmos de IA para um ambiente competitivo, onde o **Poupador** tenta coletar moedas 🪙 e evitar ladrões ao mesmo tempo. O objetivo é criar um agente com uma estratégia de sobrevivência eficiente, que fuja de ladrões com base em sensores de visão e olfato, ao mesmo tempo em que busca moedas e retorna ao banco para armazená-las com segurança 🏦.

### 🔧 Tecnologias

- **Java** ☕: Linguagem principal utilizada para o desenvolvimento do projeto.
- **POO (Programação Orientada a Objetos)**: Para modularizar o comportamento dos agentes e ambiente.

## 🗂️ Estrutura do Projeto

- **Poupador**: Classe principal que implementa a lógica de movimento do agente, incluindo sensores e estratégias para sobreviver ao ambiente.
- **Ladrão**: Classe que representa os inimigos do poupador, com habilidades de detectar e atacar o poupador.
- **Ambiente**: Classe que representa o ambiente simulado onde o poupador e os ladrões interagem.
- **Sensores**: Interfaces para o Poupador perceber o ambiente, incluindo sensores de visão e olfato.

## 📐 Regras do Jogo

- O **Poupador** 💰 tem como objetivo principal **coletar moedas** e retornar ao banco 🏦 para armazenar o dinheiro coletado.
- O **Ladrão** 🦹‍♂️ tenta capturar o Poupador se ele estiver dentro de seu alcance, fazendo o poupador perder todas as moedas.
- **Movimentos**: O Poupador e os ladrões podem se mover em quatro direções (cima, baixo, esquerda, direita).
- **Sensores**:
  - **Visão** 👀: O Poupador tem uma visão 7x7 do ambiente ao redor.
  - **Olfato** 👃: O Poupador pode detectar a presença de ladrões a uma curta distância.

## 🧠 Estratégias do Poupador

### 1. Coleta de Moedas 🪙
O Poupador detecta moedas na visão e se move em direção a elas para coletá-las.

### 2. Fuga de Ladrões 🏃‍♂️
O Poupador usa uma função de custo baseada em sensores de visão e olfato para identificar ladrões próximos e evitar direções de alto risco.

### 3. Retorno ao Banco 🏦
Quando o Poupador acumula uma quantidade significativa de moedas, ele decide voltar ao banco para depositá-las, reduzindo o risco de perda.

## ⚙️ Configuração do Ambiente

1. **Visão e Olfato**:
   - A visão permite detectar a posição de ladrões, moedas e bancos.
   - O olfato indica a presença de ladrões próximos, com uma intensidade maior quanto mais próximo o ladrão estiver.

2. **Movimentação Aleatória**:
   - Em situações onde nenhuma moeda ou ladrão está próximo, o Poupador faz movimentos aleatórios.

## 🚀 Como Executar

1. **Clone o Repositório**:
   ```bash
   git clone https://github.com/seu-usuario/ladrao-vs-poupador.git
