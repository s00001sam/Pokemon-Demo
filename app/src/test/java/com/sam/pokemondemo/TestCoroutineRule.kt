package com.sam.pokemondemo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class TestCoroutineRule(
    private val testDispatcher: ExecutorCoroutineDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)//設定Main
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()//重設Main
    }
}
