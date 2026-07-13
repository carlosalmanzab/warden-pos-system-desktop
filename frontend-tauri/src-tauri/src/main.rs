// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

use tauri::api::process::Command;

fn main() {
    tauri::Builder::default()
        .setup(|app| {
            let (mut rx, child) = Command::new_sidecar("backend")
                .expect("Error to setup Spring Boot sidecar")
                .spawn()
                .expect("Error to launch Spring Boot");
            
            Ok(())
        })
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
