import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import time 
from datetime import datetime as dt

class data:
    def __init__(self, db_reference):
        self.db_reference = db_reference
        self.make_dic()

        self.title = []
        self.get_title()

        self.challengers = dict()
        self.get_challengers()

        self.data_path = self.getDataPath()
        
    def make_dic(self):
        self.db_reference = str(self.db_reference)
        self.db_reference = eval(self.db_reference)

    def get_title(self):
        for i in self.db_reference:
            self.title.append(i)
    
    def get_challengers(self):
        for title in self.title:
            a = []
            for user in self.db_reference[title]["challengers"]:
                a.append(user)
            self.challengers[title] = a

    def getDataPath(self):
        path_list = []
        for title in self.title:
            for user in self.challengers[title]:
                path = "challenges/"+title+"/challengers/"+user+"/Data"
                path_list.append(path)
        return path_list


def check_passed(challenge_day, today):
    formattd_challenge_day = time.strptime(challenge_day, "%Y-%m-%d")
    formattd_today = time.strptime(today, "%Y-%m-%d")
    
    if(formattd_today > formattd_challenge_day): return True
    else: return False
    
    
def main(db_reference):
    data_ = data(db_reference)
    pathes = data_.data_path
    data_dic = data_.db_reference

    str_today = time.strftime('%Y-%m-%d', time.localtime(time.time()))
    for title in data_.title:
            for user in data_dic[title]["challengers"]:
                cnt = 1
                for challenge_reprot in data_dic[title]["challengers"][user]["Data"][1:]:
                    succesion = challenge_reprot["succesion"]
                    challenge_day = challenge_reprot["challenge_day"]
                    if check_passed(challenge_day, str_today) and succesion == 0:
                        print("challenge [{}] user {}'s Day {} challenge succesion updated".format(title, user, challenge_day))
                        path = "challenges/"+title+"/challengers/"+user+"/Data/{}".format(cnt)
                        update_db = db.reference(path)
                        update_db.update({"succesion" : -1})
                    cnt += 1
   

if __name__ == '__main__':
    today = time.strftime('%Y-%m-%d', time.localtime(time.time()))
    cred = credentials.Certificate('challengetogether-firebase-adminsdk-hbh4z-343fec63c7.json')
    firebase_admin.initialize_app(cred,{
        'databaseURL' : 'https://challengetogether-default-rtdb.firebaseio.com/'
    })
    while 1:
        now = dt.now().strftime("%H:%M:%S")
        print(now)
        if now == "00:00:00":
            db_reference = db.reference("challenges").get()
            print("activate updating data")
            main(db_reference)
        time.sleep(1)
        


    
    
    





 



