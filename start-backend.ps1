# Script para iniciar el backend con todas las configuraciones
# Uso: .\start-backend.ps1

Write-Host "🚀 Iniciando Backend SIP3..." -ForegroundColor Cyan
Write-Host ""

# Configurar variables de entorno inline (sin archivo .env)
$env:MONGODB_URI = 'mongodb+srv://gulincastellsmatias_db_user:zzQH5UOqXMMpwpGW@sip-3.ca4ddta.mongodb.net/sip3db?appName=SIP-3'
$env:JWT_SECRET = 'ZGVmYXVsdFNpcDNKd3RTZWNyZXRLZXlGb3JEZXZlbG9wbWVudA=='
$env:JWT_EXPIRATION_MS = '86400000'
$env:PORT = '8080'

Write-Host "✓ Variables de entorno configuradas" -ForegroundColor Green
Write-Host "  - MongoDB: Atlas (cloud)" -ForegroundColor Gray
Write-Host "  - Puerto: 8080" -ForegroundColor Gray
Write-Host ""

# Verificar si Maven está instalado
$mavenInstalled = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mavenInstalled) {
    Write-Host "⚠️  Maven no encontrado, usando wrapper..." -ForegroundColor Yellow
    .\mvnw.cmd spring-boot:run
} else {
    Write-Host "▶️  Ejecutando Maven..." -ForegroundColor Cyan
    mvn spring-boot:run
}

