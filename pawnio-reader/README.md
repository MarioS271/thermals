# pawnio-reader

A windows shim for reading hardware data using PawnIO and handing it to the main java application.

## Architecture

pawnio-reader sits between PawnIO (a universal kernel driver, here used with a few "amx modules" to get specific hardware details)
and the main java app to make it possible for the java app to easily read hardware details from PawnIO.

It does so using the following:
- PawnIO (cpu temp, fan speeds)
- NVAPI (nvidia gpu stuff)
- D3DKMTHK (non-nvidia gpu stuff, windows-builtin)

## Compatibility

| Feature          | Intel | AMD | NVIDIA | Notes                                     |
|------------------|-------|-----|--------|-------------------------------------------|
| CPU temperature  | ✅     | ✅   | -      | AMD: Zen (Ryzen 1000+) only via SMN       |
| CPU frequency    | ✅     | ✅   | -      | AMD: P-state 0 (max boost), not current   |
| GPU temperature  | ✅     | ✅   | ✅      | Via D3DKMTHK                              |
| GPU memory clock | ✅     | ✅   | ✅      | Via D3DKMTHK                              |
| GPU usage        | ❌     | ❌   | ✅      | NVAPI only (zero on non-NVIDIA)           |
| GPU fan speed    | ❌     | ❌   | ⚠️     | NVAPI only (zero on non-NVIDIA & laptops) |


## Data Sources

- **CPU temperature**: PawnIO; (`IntelMSR.bin` or `AMDFamily17.bin`/`RyzenSMU.bin`)
- **CPU frequency**: PawnIO (`IntelMSR.bin` or `AMDFamily17.bin`/`RyzenSMU.bin`)
- **GPU temperature, memory clock**: D3DKMTHK
- **GPU fan speed, usage**: NVAPI (nvidia only, D3DKMTHK as fallback attempt for non-nvidia)

## Output

Prints a single JSON object to stdout and exits:

```json
{
  "cpu_temp_c": 63.0,
  "cpu_freq_mhz": 4400,
  "gpus": [
    {
      "name": "NVIDIA GeForce RTX 4050 Laptop GPU",
      "temp_c": 47.6,
      "fan_rpm": 0,
      "gpu_usage_pct": 4.0,
      "mem_freq_mhz": 8001
    }
  ]
}
```

Errors are returned as `{"error": "<reason>"}` with a non-zero exit code.

## Requirements

- Windows 10/11 x86_64
- [PawnIO](https://pawnio.eu) installed and running (installer gives you the option to install it)
- Administrator privileges
- NVIDIA GPU driver (for NVAPI data)

## Dependencies

- [PawnIOLib](https://github.com/namazso/PawnIO) - user-mode PawnIO client
- [PawnIO.Modules](https://github.com/namazso/PawnIO.Modules) - signed hardware modules
- [NVAPI](https://github.com/NVIDIA/nvapi) - NVIDIA GPU data
