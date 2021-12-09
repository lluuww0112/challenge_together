FROM ubuntu20:0.4

MAINTAINER tgur0620@gmail.com

RUN apt update
RUN apt-get install vim
RUN apt install python3
RUN apt-get -y install python3-pip
RUN pip install -r requirement.txt

CMD ["update_challenge_data.py"]
