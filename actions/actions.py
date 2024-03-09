from typing import Any, Text, Dict, List
import pickle
from rasa_sdk import Action, Tracker
from rasa_sdk.events import SlotSet
from rasa_sdk.executor import CollectingDispatcher
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import pandas as pd
# jobs = pickle.load(open("jobs_list.pkl" , 'rb'))

# similarity = pickle.load(open("similarity.pkl", 'rb'))

class ExtractJobEntity(Action):

    def name(self) -> Text:
        return "action_extract_job_entity"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        job_entity = next(tracker.get_latest_entity_values('job'),None)

        if job_entity:
            dispatcher.utter_message(text=f"Bạn chọn nghề {job_entity} làm ví dụ")
        else :
            dispatcher.utter_message(text=f"Xin lỗi, tôi không thể tìm thấy nghề bạn nói")

        return []

class RecommendJobAction(Action):
    def name(self) -> Text:
        return "action_recommend_job"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        dispatcher.utter_message(text="Chắc rồi bạn có thể cung cấp lĩnh vực hoặc kỹ năng mà bạn có không?")
        return []

# class InformJobRecommendAction(Action):
#     def name(self) -> Text:
#         return "action_inform_job_recommend"

#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

#         job_entity = next(tracker.get_latest_entity_values('field'),None)
#         print(job_entity)
#         index = jobs[jobs['jobField'].str.contains(job_entity, case=False)].index[0]
#         distance = sorted(list(enumerate(similarity[index])), reverse=True, key=lambda vector: vector[1])
#         job_recommend = []
#         for j in distance[0:5]:
#             job_recommend.append(jobs.iloc[j[0]]['name'])
#         job_recommend_str = ', '.join(job_recommend)
#         if job_entity:
#             message = f"Đây là vài việc về {job_entity} mà tôi có thể cho bạn biết như {job_recommend_str} "
#             dispatcher.utter_message(text=message)
#         else :
#             dispatcher.utter_message(text=f"Xin lỗi, tôi không thể tìm thấy nghề bạn nói")

#         return [SlotSet("field", job_entity)]

# class RequirementJobAction(Action):
#     def name(self) -> Text:
#         return "action_requirement_job"

#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
#         field = tracker.get_slot("field")
#         index = jobs[jobs['jobField'].str.contains(field, case=False)].index[0]
#         distance = sorted(list(enumerate(similarity[index])), reverse=True, key=lambda vector: vector[1])
#         job_recommend = []
#         for j in distance[0:1]:
#             job_recommend.append(jobs.iloc[j[0]]['requirements'])
#         job_recommend_str = ', '.join(job_recommend)
#         print(job_recommend_str)
#         if field:
#             message = f"Đây là yêu cầu về công việc mà tôi có thể cho bạn biết như {job_recommend_str} "
#             dispatcher.utter_message(text=message)
#         else:
#             dispatcher.utter_message(text=f"Xin lỗi, tôi không thể tìm thấy yêu cầu về nghề này")

#         return []

# class RecommendJobFromSkillAction(Action):
#     def name(self) -> Text:
#         return "action_recommend_job_from_skill"

#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:


#         user_skill = tracker.latest_message.get('text', '').lower()
#         skill = (
#             user_skill.replace("kỹ năng", "")
#             .replace("các", "")
#             .replace("tôi", "")
#             .replace("mà", "")
#             .replace("có", "")
#             .replace("là", "")
#             .replace("sau", "")
#             .replace("như", "")
#             .replace("của", "")
#             .strip()
#         )
#         requirements_list = jobs['requirements'].tolist()
#         requirements_list.insert(4769, skill)
#         new_data = pd.Series(requirements_list)
#         cv = CountVectorizer(max_features=4769, stop_words='english')
#         vectors = cv.fit_transform(new_data.values.astype('U')).toarray()
#         similaritySkill = cosine_similarity(vectors)
#         index = new_data[new_data == skill].index[0]
#         distance = sorted(list(enumerate(similaritySkill[index])), reverse=True, key=lambda vector: vector[1])
#         job_recommend = []
#         for i in distance[1:6]:
#             job_recommend.append(jobs.iloc[i[0]]['name'])
#         job_recommend_str = ', '.join(job_recommend)
#         dispatcher.utter_message(text=f"Các công việc phù hợp với kỹ năng của bạn là {job_recommend_str} ")
#         return []