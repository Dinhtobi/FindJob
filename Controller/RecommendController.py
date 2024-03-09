from flask import Blueprint, request , jsonify ,json, Response, abort
import pickle
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import pandas as pd
import os
RecommendController = Blueprint('RecommendController', __name__ , url_prefix="/AI/recommend")


current_directory = os.path.dirname(os.path.abspath(__file__))
csv_path = os.path.join(current_directory, '..', 'data', 'Export.csv')
jobs = pd.read_csv(csv_path, delimiter=';')
jobs['tags'] = jobs['jobField'] + " " + jobs['descrition'] +" " +jobs['requirements']
new_data = jobs.drop(columns =[ 'comanyId','comanyName','exerience' ])
cv = CountVectorizer(max_features=4768, stop_words='english')
vector = cv.fit_transform(new_data['tags'].values.astype('U')).toarray()
similarity = cosine_similarity(vector)

def convert_int64_to_int(value):
    return int(value)
def recommand(jobField,number):
    try:
        json_data = []  
        for (i, field), (i,n) in zip(enumerate(jobField), enumerate(number)):
            index = jobs[jobs['jobField'] == field].index[0]
            distance = sorted(list(enumerate(similarity[index])), reverse=True, key=lambda vector: vector[1])
            for j in distance[0:5 * n]:
                json_data.append(jobs.iloc[j[0]]['id'])
        converted_list = [convert_int64_to_int(value) for value in json_data]
        json_list = {"ids" : converted_list}
        return json.dumps(json_list, ensure_ascii=False)
    except:
        json_object = {"ids": []}
        json_data = json.dumps(json_object)
        return json_data

@RecommendController.route('/field' , methods = ['POST'])
def Job():

    request_list = request.json
    jobField = [item['jobField'] for item in request_list]
    numberJob = [item['number'] for item in request_list]
    job_recommend = recommand(jobField,numberJob)
    return job_recommend

@RecommendController.route("/skill", methods = ['POST'])
def skillRecommend():
    try:
        your_skills = request.json['name']
        requirements_list = jobs['requirements'].tolist()
        requirements_list.insert(4769, your_skills)
        new_data = pd.Series(requirements_list)
        cv = CountVectorizer(max_features=4769, stop_words='english')
        vectors = cv.fit_transform(new_data.values.astype('U')).toarray()
        similaritySkill = cosine_similarity(vectors)
        index = new_data[new_data == your_skills].index[0]
        distance = sorted(list(enumerate(similaritySkill[index])), reverse=True, key=lambda vector: vector[1])
        job_recommend = []
        for i in distance[1:10]:
            job_recommend.append(jobs.iloc[i[0]]['id'])
        converted_list = [convert_int64_to_int(value) for value in job_recommend]
        json_object = {"ids": converted_list}
        return json.dumps(json_object, ensure_ascii=False)
    except:
        json_object = {"ids": []}
        json_data = json.dumps(json_object)
        return json_data
