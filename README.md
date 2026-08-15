# Thermals

A lightweight system monitor that lives in your Windows system tray, built with Java Swing and a dark JetBrains-inspired look.

The tray icon changes color in real time based on your CPU temperature (green when cool, red when hot). Left-click it to open a compact popup with live stats and graphs for CPU, GPU, RAM, disk, and network.

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

| Library/Tool                                    | Purpose                |
|-------------------------------------------------|------------------------|
| AWT and Swing                                   | Tray Icon and Popup UI |
| [FlatLaf](https://www.formdev.com/flatlaf/)     | Dark look-and-feel     |
| [JFreeChart](https://www.jfree.org/jfreechart/) | Live history graphs    |
| OSHI, PawnIO, NVAPI, nvidia-smi, hwmon, ...     | Hardware & sensor data |

---

## Data Sources

| Component                     | Windows          | Linux                             |
|-------------------------------|------------------|-----------------------------------|
| CPU usage, RAM, disk, network | OSHI             | OSHI                              |
| CPU temp & freq               | PawnIO           | OSHI                              |
| GPU data (NVIDIA)             | NVAPI & D3DKMTHK | `nvidia-smi`                      |
| GPU data (AMD)                | D3DKMTHK         | sysfs hwmon (`/sys/class/hwmon/`) |

---

## Supported Platforms

| OS      | Supported? |
|---------|------------|
| Windows | Yes        |
| Linux   | Not Yet    |
| macOS   | No         |

> If you are willing to add support for any of the unsupported platforms, I'd be happy to merge a PR!

## Requirements

- Java 17+

---

## Building

```bash
./gradlew build
```

Run with:

```bash
./gradlew run
```

The app starts minimized to the system tray. Left-click the icon to open the stats window. Right-click for a context menu with an exit option.

---

## AI Usage Disclaimer

I used AI as a mentor for working through UI layout decisions and architecture questions. All code was written by me.
I also used it for writing docs, such as parts of this README you're reading right now.
Additionally, I used it for assisting with research on the topic of reading sensor data on Windows, cause microsoft
makes it sooooo easy and definitely provides a standardized model for accessing hardware metrics :)))))

---

## License

Licensed under the **GNU General Public License v3.0** (`GPL-3.0-only`). See [LICENSE](LICENSE) for details.
