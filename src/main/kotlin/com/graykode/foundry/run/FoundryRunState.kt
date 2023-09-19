package com.graykode.foundry.run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.autotest.ToggleAutoTestAction
import com.intellij.openapi.diagnostic.Logger


class FoundryRunState(private val runConfiguration: FoundryRunConfiguration, environment: ExecutionEnvironment) :
    CommandLineState(environment) {
    private val LOG = Logger.getInstance(FoundryRunState::class.java)

    override fun startProcess(): ProcessHandler {
        val workingDir = environment.project.basePath
        val testArgumentKey = runConfiguration.testArgumentKey
        val testArgumentValue = runConfiguration.testArgumentValue
        LOG.info("FoundryRunState: $workingDir ${runConfiguration.additionalArguments} ${runConfiguration.environmentVariables}")
        val commandLine = when {
            testArgumentKey.isEmpty() || testArgumentValue.isEmpty() || workingDir == null -> {
                GeneralCommandLine()
            }
            else -> {
                val commands: List<String> = listOf(
                    "forge", "test", testArgumentKey, testArgumentValue.replace("$workingDir/", ""), "--root", workingDir,
                )
                GeneralCommandLine(
                    if (runConfiguration.additionalArguments.joinToString(" ").trim().isNotEmpty()) {
                        commands + runConfiguration.additionalArguments
                    } else {
                        commands
                    }
                )
            }
        }

        val handler = KillableColoredProcessHandler(commandLine.withEnvironment(
            runConfiguration.environmentVariables.envs)
        )
        ProcessTerminatedListener.attach(handler) // shows exit code upon termination
        return handler
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        val processHandler = startProcess()
        val console = createConsole(executor)
        console?.attachToProcess(processHandler)
        return DefaultExecutionResult(console, processHandler).apply { setRestartActions(ToggleAutoTestAction()) }
    }
}