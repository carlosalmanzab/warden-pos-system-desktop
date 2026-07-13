set shell := ["bash", "-c"]

dev-backend:
    cd backend && ./gradlew bootRun

dev-tauri:
    cd frontend && pnpm tauri dev

dev:
    dev-backend dev-tauri

build-backend-native:
    cd backend && ./mvnw clean native:compile -Pnative
    mkdir -p src-tauri/binaries

clean-all:
    cd backend && ./gradlew clean
    cd frontend-tauri && pnpm tauri clean
    rm -rf frontend-tauri/src-tauri/binaries/wardenpos-backend*



