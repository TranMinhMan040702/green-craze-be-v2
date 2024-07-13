FROM python:3.10

RUN apt-get update
RUN apt-get install gcc libc-dev g++ libffi-dev libxml2 libffi-dev unixodbc-dev -y

WORKDIR /app

COPY . .

RUN pip install -r requirements.txt

CMD ["python", "run.py"]