# 💲Conversor de Moedas💲

Um conversor de moedas simples e eficiente que permite a conversão entre 9 tipos de moedas diferentes e gera um histórico de uso.

## Índice

- [Sobre](#sobre)
- [Funcionalidades](#funcionalidades)
- [Moedas Suportadas](#moedas-suportadas)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação](#instalação)
- [Uso](#uso)
- [Screenshot](#screenshot)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Sobre

Este conversor de moedas foi desenvolvido para facilitar a conversão entre diferentes moedas, permitindo que o(a) usuário(a) realize conversões rápidas e acessíveis. Além disso, o aplicativo mantém um histórico de todas as conversões realizadas, para que o(a) usuário(a) possa acompanhar suas atividades.

## Funcionalidades

- Conversão entre 9 tipos de moedas diferentes.
- Geração de histórico de conversões, registrando todas as operações do(a) usuário(a).
- Interface amigável e fácil de usar.

## Moedas Suportadas

Esse conversor funciona com os seguintes tipos de moedas:

1. BRL (Real brasileiro)
2. ARS (Peso argentino)
3. BOB (Boliviano boliviano)
4. CLP (Peso chileno)
5. COP (Peso colombiano)
6. USD (Dólar americano)
7. CAD (Dólar canadense)
8. JPY (Iene japonês)
9. CNY (Yuan chinês)<br>


O conversor permite a conversão entre todas essas moedas e pode ser facilmente expandido para suportar mais moedas no futuro.

## Tecnologias Utilizadas

- Linguagem: Java
- API: [ExchangeRate-API](https://www.exchangerate-api.com) - Free & Pro Currency Converter API
- Ferramentas: Git

## Instalação

Antes de rodar a aplicação, você precisa instalar o Java JDK 21 ou superior.
[Link para download do JDK 21.](https://www.oracle.com/java/technologies/downloads/?er=221886#jdk21-windows)

Siga os passos abaixo para baixar e configurar a aplicação localmente:


1. **Clone o repositório:**
   ```bash
   git clone https://github.com/rquinalha/Conversor-Moedas
   ```
2. **Navegue até o diretório do projeto:**
   ```bash
   cd "local onde baixou o projeto\Conversor Moedas"
   ```
3. **Abra a pasta da aplicação via terminal e compile os arquivos Java:**
   ```bash
   javac -cp lib\gson-2.11.0.jar -d bin src\*.java
   ```
4. **Após isso, execute o comando abaixo para iniciar a aplicação:**
   ```bash
   java -cp ".;lib\gson-2.11.0.jar;bin" Main
   ```
5. **Insira a API Key:**
- Para conseguir uma API Key, você precisa se cadastrar no site da [ExchangeRate-API](https://www.exchangerate-api.com) e clicar em <kbd>Get Free Key!</kbd>
- Com sua API Key em mãos, basta copiar e colar os dados da sua Key no terminal.

## Uso

Após executar o programa pelo terminal, é só seguir as instruções para selecionar as moedas e inserir o valor a ser convertido.

O histórico de conversões será salvo e exibido no terminal caso deseje.

## Screenshot

![App Screenshot](https://github.com/rquinalha/Outros/blob/main/app.png)


## Contribuição
Contribuições são bem-vindas!

Sinta-se à vontade para sugerir melhorias, correções de bugs ou novas funcionalidades.

## Licença
Este projeto está licenciado sob a MIT License.

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## Desenvolvedor

[<img src="https://avatars.githubusercontent.com/u/173571909?s=400&v=4" width=115><br><sub>Rafael Quinalha</sub>](https://github.com/rquinalha)