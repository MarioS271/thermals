# Thermals

A lightweight system monitor, built with Java Swing and a dark JetBrains-inspired look.
It features an informative UI aswell as a tray icon (if supported by your DE/WM/OS) which changes color
depending on the current CPU temperature.

---

## Features

- Temperature-driven tray icon (green to red color gradient)
- Live per-component panels with scrolling history graphs
- CPU usage, frequency, temperature, and core stats
- GPU, RAM, disk, and network monitoring panels
- Dark theme with rounded cards
- Overflow error if you run the app for a few years :)

---

## Tech Stack

| Library/Tool              | Purpose                    |
|---------------------------|----------------------------|
| AWT and Swing             | Tray Icon and Popup UI     |
| FlatLaf                   | Dark look-and-feel         |
| JFreeChart                | Charts                     |
| OSHI, PawnIO, NVAPI, ADLX | Data Monitoring on Windows |
| OSHI, nvidia-smi, hwmon   | Data Monitoring on Linux   |

---

## Data Sources

| Component         | Windows         | Linux        |
|-------------------|-----------------|--------------|
| CPU temp          | PawnIO          | OSHI         |
| GPU data (NVIDIA) | NVAPI, D3DKMTHK | `nvidia-smi` |
| GPU data (AMD)    | ADLX, D3DKMTHK  | sysfs hwmon  |
| All other stats   | OSHI            | OSHI         |

---

## Supported Platforms

| OS      | Supported? |
|---------|------------|
| Windows | Yes        |
| Linux   | Yes        |
| macOS   | No         |

> If you are willing to add support for any of the unsupported platforms, I'd be happy to merge a PR!

## Requirements

- JBR 25 (Jetbrains Runtime)
- PawnIO Kernel Driver (Windows only)

---

## AI Usage Disclaimer

I used AI as a mentor for working through UI layout decisions and architecture questions. All code was written by me.
I also used it for writing docs, such as parts of this README you're reading right now.
Additionally, I used it for assisting with research on the topic of reading sensor data on Windows, cause microsoft
makes it sooooo easy and definitely provides a standardized model for accessing hardware metrics :)))))

---

## License

Licensed under the **GNU General Public License v3.0** (`GPL-3.0-only`). See [LICENSE](LICENSE) for details.
