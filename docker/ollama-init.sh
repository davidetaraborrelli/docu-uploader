#!/bin/bash
ollama serve &
echo "Attendo avvio Ollama server..."
sleep 5
echo "Pull modello nomic-embed-text..."
ollama pull nomic-embed-text
echo "Ollama pronto con modello nomic-embed-text"
wait
