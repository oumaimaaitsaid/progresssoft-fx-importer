
.PHONY: start stop clean build logs

start:
	@echo "Building images and starting containers (App + DB)..."
	docker-compose up --build

stop:
	@echo "Stopping containers..."
	docker-compose down

clean:
	@echo "Stopping containers and REMOVING ALL DATA..."
	docker-compose down -v

logs:
	@echo "Following logs for fx-app..."
	docker-compose logs -f app

build:
	@echo "Running local Maven build and tests..."
	./mvnw clean package