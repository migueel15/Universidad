FROM python:3.11-slim

ENV PYTHONUNBUFFERED=1

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY pr3_kazoo.py .
COPY init_config.py .

ENTRYPOINT ["python", "pr3_kazoo.py"]

