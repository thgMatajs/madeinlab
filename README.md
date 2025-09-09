# 🚀 Tela de Login - MadeInLab

Este repositório contém a implementação de uma tela de login nativa para Android. O projeto foi construído do zero com foco em boas práticas de
desenvolvimento, arquitetura limpa, componentização e uma estratégia de testes robusta.

### 🎨 **Design**

![ui_mock](images/ui_mock.png)

### 🎥 Demonstração

![login_demo](images/demo.webm)

---

## 🛠️ Tecnologias e Ferramentas

Este projeto utiliza um conjunto de tecnologias modernas do ecossistema Android para garantir um desenvolvimento escalável, testável e de alta
qualidade.

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) para uma UI declarativa e moderna.
* **Arquitetura:** Clean Architecture + MVVM.
* **Injeção de Dependência:** [Koin](https://insert-koin.io/) para gerenciar as dependências de forma simples e eficaz.
* **Assincronismo:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-guide.html) para gerenciar operações em background e fluxos de
  dados reativos.
* **Testes:**
    * [JUnit4](https://junit.org/junit4/): Framework base para testes.
    * [MockK](https://mockk.io/): Para criação de mocks e spies em testes unitários.
    * [Truth](https://truth.dev/): Biblioteca de asserções do Google para testes mais legíveis.
    * [Turbine](https://github.com/cashapp/turbine): Para testar `StateFlow` de forma simples e eficiente.
    * [Compose Test Rule](https://developer.android.com/jetpack/compose/testing): Para testes de UI e de integração com Jetpack Compose.

---

## 🏛️ Arquitetura

O projeto segue os princípios da **Clean Architecture**, separando o código em camadas independentes com responsabilidades bem definidas. Isso promove
um baixo acoplamento, alta coesão e facilita a manutenção e os testes.

O fluxo de dependência é sempre de fora para dentro: **Apresentação -> Domínio <- Dados**.

```
 Camada de Apresentação (UI)
         |
         v
 Camada de Domínio (Regras de Negócio)
         ^
         |
 Camada de Dados (Fontes de Dados)
```

### 🎨 Camada de Apresentação (Presentation)

Responsável por toda a interação com o usuário. Utiliza o padrão **MVVM (Model-View-ViewModel)**.

* **View (`LoginScreen` & `LoginScreenContent`):**
    * Construída com Jetpack Compose.
    * `LoginScreen`: Composable "inteligente" (stateful), responsável por observar o `ViewModel`, gerenciar o ciclo de vida, foco e coletar eventos de
      UI.
    * `LoginScreenContent`: Composable "burro" (stateless), responsável apenas por renderizar o estado (`LoginUiState`) e emitir eventos (
      `LoginActions`). É totalmente desacoplado da lógica de negócio, o que o torna extremamente fácil de testar e reutilizar.
* **ViewModel (`LoginViewModel`):**
    * Atua como a "fonte da verdade" para a UI.
    * Expõe o estado da tela através de um `StateFlow<LoginUiState>`.
    * Recebe as ações do usuário, delega a lógica de negócio para os `UseCases` e atualiza o estado da UI.
    * Não possui nenhuma referência ao framework do Android (exceto `ViewModel` do `androidx.lifecycle`).

### 🧠 Camada de Domínio (Domain)

O coração da aplicação. Contém a lógica de negócio pura, sem nenhuma dependência do framework do Android.

* **Use Cases (Casos de Uso):**
    * Cada `UseCase` tem uma única responsabilidade, seguindo o Princípio da Responsabilidade Única (SRP).
    * **`ValidateEmailUseCase`**: Responsável por validar se uma string está em um formato de e-mail válido. Retorna um `ValidationResult`.
    * **`ValidatePasswordUseCase`**: Responsável por validar se uma senha atende aos critérios de negócio (ex: tamanho mínimo). Retorna um
      `ValidationResult`.
    * **`LoginUseCase`**: Orquestra o processo de login. Ele invoca os `UseCases` de validação e, se ambos passarem, chama o `LoginRepository` para
      autenticar o usuário.
* **Repository (Interface):**
    * Define o contrato (`LoginRepository`) que a camada de dados deve implementar. A camada de domínio não conhece a implementação, apenas a
      abstração.
* **Models:**
    * Classes de dados puras (ex: `ValidationResult`) que representam as entidades de negócio.

### 💾 Camada de Dados (Data)

Responsável por buscar e salvar dados de diferentes fontes (API, banco de dados local, etc.).

* **Repository (Implementação):**
    * Implementa a interface `LoginRepository` definida na camada de domínio.
    * *Neste projeto, a implementação concreta (ex: `LoginRepositoryImpl` que faria uma chamada de API com Retrofit) foi omitida, mas a estrutura está
      pronta para recebê-la.*

---

## 🧪 Estratégia de Testes

A arquitetura adotada permite uma estratégia de testes completa e eficiente, cobrindo todas as camadas da aplicação.

* **✅ Testes Unitários (Camada de Domínio):**
    * Os `UseCases` são testados em isolamento na JVM.
    * Utilizamos `MockK` para mockar dependências (como o repositório).
    * São testes rápidos e garantem que a lógica de negócio está correta.

* **✅ Testes Unitários (Camada de Apresentação):**
    * O `LoginViewModel` é testado na JVM.
    * Utilizamos `MockK` para mockar os `UseCases` e `Turbine` para testar as emissões do `StateFlow`, garantindo que o estado da UI é atualizado
      corretamente em resposta às ações e resultados dos `UseCases`.

* **✅ Testes de UI (Instrumentados):**
    * **Testes de Componentes:** O `LoginScreenContent` (nosso componente stateless) é testado em isolamento. Verificamos se ele renderiza todos os
      estados possíveis (erro, loading, botão desabilitado) corretamente.
    * **Testes de Integração de UI:** O `LoginScreen` (nosso componente stateful) é testado em conjunto com um `LoginViewModel` real, onde os
      `UseCases` são mockados usando `KoinTestRule`. Isso valida o fluxo completo: o usuário digita -> o `ViewModel` é notificado -> o `UseCase` é
      chamado -> o estado da UI é atualizado -> a UI exibe o erro.

---

## 🚀 Como Executar

1. Clone este repositório: `git clone <https://github.com/thgMatajs/madeinlab.git>`
2. Abra o projeto no Android Studio.
3. Aguarde o Gradle sincronizar as dependências.
4. Execute o aplicativo em um emulador ou dispositivo físico.
5. Para rodar os testes, navegue até os diretórios `test` e `androidTest` e execute as classes de teste.