param([Parameter(ValueFromRemainingArguments=$true)][string[]]$Tasks)
$ErrorActionPreference='Stop'
if (-not $Tasks) { $Tasks=@('assembleDebug',':pr08:testDebugUnitTest') }
Push-Location $PSScriptRoot
try {
    & '.\gradlew.bat' @Tasks --console=plain
    if ($LASTEXITCODE -ne 0) { throw "Gradle exit code: $LASTEXITCODE" }
} finally { Pop-Location }
