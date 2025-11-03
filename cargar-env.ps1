# Script para cargar variables de entorno desde .env
# Uso: . .\cargar-env.ps1

$envFile = Join-Path $PSScriptRoot ".env"

if (Test-Path $envFile) {
    Write-Host "📂 Cargando variables desde .env..." -ForegroundColor Cyan
    
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.+)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            Set-Item -Path "env:$name" -Value $value
            Write-Host "  ✓ $name" -ForegroundColor Green
        }
    }
    
    Write-Host "✅ Variables cargadas exitosamente!" -ForegroundColor Green
} else {
    Write-Host "❌ No se encontró el archivo .env" -ForegroundColor Red
    Write-Host "Copia env.example a .env primero" -ForegroundColor Yellow
}

