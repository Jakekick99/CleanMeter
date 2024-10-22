package br.com.firstsoft.target.server.ui.tabs.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.firstsoft.target.server.ui.components.CheckboxSection
import br.com.firstsoft.target.server.ui.components.DropdownSection
import ui.app.OverlaySettings
import java.awt.GraphicsEnvironment

private fun List<CheckboxSectionOption>.filterOptions(vararg optionType: SettingsOptionType) =
    this.filter { source -> optionType.any { it == source.type } }

@Composable
fun OverlaySettingsUi(
    overlaySettings: OverlaySettings,
    onOverlaySettings: (OverlaySettings) -> Unit,
) = Column(
    modifier = Modifier.padding(bottom = 8.dp, top = 20.dp).verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    val screenDevices = remember { GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices }

    val availableOptions = remember(overlaySettings) {
        listOf(
            CheckboxSectionOption(
                isSelected = overlaySettings.fps,
                name = "Frame count",
                type = SettingsOptionType.Framerate
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.frametime,
                name = "Frame time graph",
                type = SettingsOptionType.Frametime
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.cpuTemp,
                name = "CPU temperature",
                type = SettingsOptionType.CpuTemp
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.cpuUsage,
                name = "CPU usage",
                type = SettingsOptionType.CpuUsage
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.gpuTemp,
                name = "GPU temperature",
                type = SettingsOptionType.GpuTemp
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.gpuUsage,
                name = "GPU usage",
                type = SettingsOptionType.GpuUsage
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.vramUsage,
                name = "Vram usage",
                type = SettingsOptionType.VramUsage
            ),
            CheckboxSectionOption(
                isSelected = overlaySettings.ramUsage,
                name = "Ram usage",
                type = SettingsOptionType.RamUsage
            ),
        )
    }

    fun onOptionsToggle(option: CheckboxSectionOption) {
        val newSettings = when (option.type) {
            SettingsOptionType.Framerate -> overlaySettings.copy(fps = option.isSelected)
            SettingsOptionType.Frametime -> overlaySettings.copy(frametime = option.isSelected)
            SettingsOptionType.CpuTemp -> overlaySettings.copy(cpuTemp = option.isSelected)
            SettingsOptionType.CpuUsage -> overlaySettings.copy(cpuUsage = option.isSelected)
            SettingsOptionType.GpuTemp -> overlaySettings.copy(gpuTemp = option.isSelected)
            SettingsOptionType.GpuUsage -> overlaySettings.copy(gpuUsage = option.isSelected)
            SettingsOptionType.VramUsage -> overlaySettings.copy(vramUsage = option.isSelected)
            SettingsOptionType.RamUsage -> overlaySettings.copy(ramUsage = option.isSelected)
        }
        onOverlaySettings(newSettings)
    }

    CheckboxSection(title = "FPS",
        options = availableOptions.filterOptions(SettingsOptionType.Framerate, SettingsOptionType.Frametime),
        onOptionToggle = ::onOptionsToggle,
        onSwitchToggle = {
            onOverlaySettings(overlaySettings.copy(fps = it, frametime = it))
        }
    )

    CheckboxSection(title = "GPU",
        options = availableOptions.filterOptions(
            SettingsOptionType.GpuUsage,
            SettingsOptionType.GpuTemp,
            SettingsOptionType.VramUsage
        ),
        onOptionToggle = ::onOptionsToggle,
        onSwitchToggle = { onOverlaySettings(overlaySettings.copy(gpuTemp = it, gpuUsage = it, vramUsage = it)) }
    )

    CheckboxSection(title = "CPU",
        options = availableOptions.filterOptions(SettingsOptionType.CpuUsage, SettingsOptionType.CpuTemp),
        onOptionToggle = ::onOptionsToggle,
        onSwitchToggle = { onOverlaySettings(overlaySettings.copy(cpuTemp = it, cpuUsage = it)) }
    )

    CheckboxSection(title = "RAM",
        options = availableOptions.filterOptions(SettingsOptionType.RamUsage),
        onOptionToggle = ::onOptionsToggle,
        onSwitchToggle = { onOverlaySettings(overlaySettings.copy(ramUsage = it)) }
    )

    DropdownSection(title = "MONITOR",
        options = screenDevices.map { it.defaultConfiguration.device.iDstring },
        selectedIndex = overlaySettings.selectedDisplayIndex,
        onValueChanged = { onOverlaySettings(overlaySettings.copy(selectedDisplayIndex = it)) }
    )
}