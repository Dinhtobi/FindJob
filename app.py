from flask import Flask , request ,render_template , jsonify
import requests
from flask_cors import CORS , cross_origin
from Controller.RecommendController import RecommendController

RASA_API_URL = 'http://localhost:5005/webhooks/rest/webhook'

app = Flask(__name__)
CORS(app)

app.config['CORS_HEADERS'] = 'Content-Type'

app.register_blueprint(RecommendController)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/webhook', methods=['POST'])
def webhook():
    user_message = request.json['message']
    print("User message: " , user_message)
    rasa_response = requests.post(RASA_API_URL , json ={'message' : user_message})
    rasa_response_json = rasa_response.json()

    print("Rasa response: " , rasa_response_json)
    bot_response = rasa_response_json[0]['text']  if rasa_response_json else 'Sorry, I did not understand'
    return jsonify({'response' : bot_response})

if __name__ == '__main__':
    app.run(host='0.0.0.0' , port= '6868')


