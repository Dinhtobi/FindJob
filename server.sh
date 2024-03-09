cd app/ && rasa train -p $PORT

cd app/ && rasa shell -p $PORT

cd /app && rasa run actions -p $PORT

cd /app && python run app.py -p $PORT