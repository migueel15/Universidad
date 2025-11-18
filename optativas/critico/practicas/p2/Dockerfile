FROM python:3.10-slim

WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
ENV PORT=80
ENV HOST=0.0.0.0
EXPOSE 80

CMD ["sh", "-c", "uvicorn api.main:app --host $HOST --port $PORT"]

