package com.example

import com.example.model.DEFAULT_SERVERS
import com.example.model.PhoneApp
import com.example.viewmodel.VirtualPhoneViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun defaultVpnServersAreConfigured() {
        assertTrue(DEFAULT_SERVERS.isNotEmpty())
        val amsterdamServer = DEFAULT_SERVERS.find { it.city == "Amsterdam" }
        assertNotNull(amsterdamServer)
        assertEquals("Netherlands", amsterdamServer?.country)
    }

    @Test
    fun vpnViewModelStateTransitions() {
        val viewModel = VirtualPhoneViewModel()
        assertFalse(viewModel.vpnState.value.isConnected)

        // Select a different server
        val zurich = DEFAULT_SERVERS.find { it.city == "Zurich" }!!
        viewModel.selectServer(zurich)
        assertEquals("Zurich", viewModel.vpnState.value.selectedServer.city)

        // Test browser tabs
        assertEquals(1, viewModel.tabs.value.size)
        viewModel.openNewTab("https://ipinfo.io")
        assertEquals(2, viewModel.tabs.value.size)

        // Test app switching
        viewModel.openApp(PhoneApp.CHROME)
        assertEquals(PhoneApp.CHROME, viewModel.phoneState.value.currentApp)
        viewModel.pressHome()
        assertEquals(PhoneApp.HOME, viewModel.phoneState.value.currentApp)
    }
}
