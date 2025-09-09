package io.gentalha.code.madeinlab.core.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Uma [TestWatcher] do JUnit que substitui o [Dispatchers.Main] por um [TestDispatcher]
 * durante a execução de testes unitários.
 *
 * ViewModels e outros componentes da camada de apresentação geralmente usam `viewModelScope.launch`,
 * que, por padrão, utiliza o `Dispatchers.Main`. Em um ambiente de teste unitário (JVM),
 * o `Dispatchers.Main` não está disponível, o que causaria uma exceção.
 *
 * Esta regra intercepta a execução do teste e injeta um dispatcher de teste, permitindo que
 * as corrotinas sejam executadas de forma síncrona e controlada.
 *
 * ### Exemplo de Uso:
 * ```
 * @ExperimentalCoroutinesApi
 * class MyViewModelTest {
 *
 *     @get:Rule
 *     val mainDispatcherRule = MainDispatcherRule()
 *
 *     // ... seus testes aqui ...
 * }
 * ```
 *
 * @param testDispatcher O [TestDispatcher] a ser usado. O padrão é [UnconfinedTestDispatcher],
 * que executa as corrotinas imediatamente na thread atual, sendo ideal para a maioria dos
 * testes de ViewModel onde o controle de tempo não é necessário. Para testes que precisam
 * de controle explícito sobre o tempo e a execução de corrotinas, um [StandardTestDispatcher]
 * pode ser injetado.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    /**
     * Chamado pelo JUnit antes da execução de cada teste.
     * Substitui o dispatcher principal padrão pelo [testDispatcher] fornecido.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Chamado pelo JUnit após a conclusão de cada teste.
     * Limpa a substituição do dispatcher principal, restaurando o comportamento padrão
     * para garantir o isolamento entre os testes.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}