NPM ?= npm
DOCS_ROOT := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))

.PHONY: help docs-install docs-serve docs-build docs-clean

help:
	@echo "Comandos disponibles:"
	@echo "  make docs-install Instala las dependencias de VitePress"
	@echo "  make docs-serve   Sirve la documentación con recarga automática"
	@echo "  make docs-build   Genera el sitio y el índice de búsqueda"
	@echo "  make docs-clean   Elimina la documentación generada"

docs-install:
	cd $(DOCS_ROOT) && $(NPM) ci

docs-serve:
	cd $(DOCS_ROOT) && $(NPM) run dev

docs-build:
	cd $(DOCS_ROOT) && $(NPM) run build

docs-clean:
	rm -rf $(DOCS_ROOT)site
