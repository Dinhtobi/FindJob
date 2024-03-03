from flask import Blueprint, request , jsonify ,json, Response, abort
import pickle
import numpy as np
RecommendController = Blueprint('RecommendController', __name__ , url_prefix="/AI/recommend")


jobs = pickle.load(open("jobs_list.pkl" , 'rb'))

similarity = pickle.load(open("similarity.pkl", 'rb'))


def convert_int64_to_int(value):
    return int(value)
def recommand(jobField,number):
    try:
        json_data = []  
        for (i, job), (i,n) in zip(enumerate(jobField), enumerate(number)):
            # print(job,n)
            index = jobs[jobs['jobField'] == job].index[0]
            distance = sorted(list(enumerate(similarity[index])), reverse=True, key=lambda vector: vector[1])
            job_recommend = []
            for j in distance[0:5 * n]:
                job_recommend.append(jobs.iloc[j[0]]['id'])
            converted_list = [convert_int64_to_int(value) for value in job_recommend]
            json_object = {"name" :job, "idPost" :converted_list}
            # json_data.update(json_object)
            json_data.append(json_object)
        json_list = {"recommendIds" : json_data}
        return json.dumps(json_list, ensure_ascii=False)
    except:
        json_object = {"ids": []}
        json_data = json.dumps(json_object)
        return json_data


@RecommendController.route('/' , methods = ['POST'])
def Job():

    request_list = request.json
    jobField = [item['jobField'] for item in request_list]
    numberJob = [item['number'] for item in request_list]
    job_recommend = recommand(jobField,numberJob)
    return job_recommend