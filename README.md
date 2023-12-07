# Livraria - Java (Spring Boot)

## Pessoa (http://localhost:8080/pessoa)

### [GET]

### [POST]

```
{
    "nome": "Daniel Toledo"
}
```

## Livro (http://localhost:8080/livro)

### [GET]

### [POST]

```
{
    "nome": "Livro 1",
    "quantidade": 32,
    "preco": 99.49
}
```

## Venda (http://localhost:8080/venda)

### [GET]

### [POST]

```
{
   "idCliente": "f0a6e728-8482-47c8-b6ef-d05004a180a5",
   "data": "2023-10-06",
   "itens": [
      {
         "livro": "9cf932a9-0d7b-4f8f-9767-db8aa0ec84bd",
         "quantidade": 10
      },
      {
         "livro": "9cf932a9-0d7b-4f8f-9767-db8aa0ec84bd",
         "quantidade": 2
      }
   ]
}
```
