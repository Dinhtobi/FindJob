cd app/ && rasa run --model /app/models --enable-api --cors "*" --debug -p $PORT

cd /app && rasa run actions -p $PORT

cd /app && python app -p $PORT