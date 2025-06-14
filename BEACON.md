Here is your updated `README` with a fully integrated `beacon.sh` setup using `btmgmt` and a unique name `nbk-<ID>` as the broadcast payload:

---

# README

## Overview

1. Flash Raspberry Pi OS
2. Set up SSH and Wi-Fi
3. Install needed tools
4. Write the `beacon.sh` script
5. Test the script
6. Set it to run on boot

---

## Step-by-Step Guide

### 1. Flash Raspberry Pi OS

Use the [**Raspberry Pi Imager**](https://www.raspberrypi.com/software/) to flash:

* **OS:** Raspberry Pi OS Lite (64-bit) is ideal.
* In “Advanced Options” (click gear icon before writing the image):

  * Enable SSH
  * Set Wi-Fi (SSID, password)
  * Set hostname (e.g., `pi-zero`)
  * Set username/password (e.g., user `admin`)

Insert the SD card into your Pi Zero and boot.

---

### 2. SSH In

Once the Pi boots and connects to Wi-Fi, SSH into it:

```bash
ssh admin@pi-zero.local
```

If `.local` doesn’t resolve, use the IP instead.

---

### 3. Install Required Packages

Run:

```bash
sudo apt update
sudo apt install -y bluez btmgmt
```

---

### 4. Create `beacon.sh`

```bash
nano ~/beacon.sh
```

Paste this script (broadcasts `nbk-1821` as both name and payload):

```bash
#!/bin/bash

# Custom ID
UNIQUE_ID="1821"
DEVICE_NAME="nbk-$UNIQUE_ID"

# Convert to hex: "nbk-1821" → 6E626B2D31383231
PAYLOAD_HEX=$(echo -n "$DEVICE_NAME" | xxd -p | tr -d '\n')

# Reset BLE
sudo btmgmt -i hci0 power off
sudo btmgmt -i hci0 power on

# Set advertised device name
sudo btmgmt -i hci0 name "$DEVICE_NAME"

# Enable advertising
sudo btmgmt -i hci0 advertising off
sudo btmgmt -i hci0 advertising on

# Broadcast using Service Data UUID 0xFEAA
sudo btmgmt -i hci0 add-adv -d -u 0xFEAA -D "$PAYLOAD_HEX"
```

Make it executable:

```bash
chmod +x ~/beacon.sh
```

---

### 5. Test It Manually

Before running, stop the default Bluetooth service to avoid conflicts:

```bash
sudo systemctl stop bluetooth
```

Then run the script:

```bash
sudo ~/beacon.sh
```

Use **nRF Connect** (Android/iOS) to scan for the beacon.
You should see:

* Name: `nbk-1821`
* Service Data (UUID `0xFEAA`): contains `nbk-1821`

---

### 6. Run Script on Boot with `systemd`

Create a service unit file:

```bash
sudo nano /etc/systemd/system/beacon.service
```

Paste:

```ini
[Unit]
Description=BLE Beacon Advertiser
After=bluetooth.target
Wants=bluetooth.target

[Service]
Type=oneshot
ExecStart=/home/admin/beacon.sh
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

Then enable and activate:

```bash
sudo systemctl daemon-reload
sudo systemctl enable beacon.service
```

---

### 7. Reboot to Verify

```bash
sudo reboot
```

Once it reboots, scan again with nRF Connect to confirm the beacon is broadcasting.

---

## Optional: Disable Conflicting Bluetooth Service

If `bluetoothd` interferes, disable it:

```bash
sudo systemctl disable bluetooth
sudo systemctl mask bluetooth
```

You can always re-enable it later with:

```bash
sudo systemctl unmask bluetooth
sudo systemctl enable bluetooth
```

---

Let me know if you want to:

* Use a random ID on boot
* Load the ID from a file
* Broadcast a URL instead
