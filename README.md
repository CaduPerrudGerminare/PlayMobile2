# 🚗 Controle de Acesso de Veículos da JBS

Este é um aplicativo Android simples desenvolvido em Java que permite:

- Cadastrar placas de veículos.
- Acessar uma área secreta exclusiva para administradores.
- Validar o acesso por senha.
- Gerenciar a exibição de telas com base no tipo de usuário.

---

## ✨ Funcionalidades

🔐 **Login de Usuário**  
- Autenticação baseada em email e senha.  

🧾 **Cadastro de Placas**  
- Tela para o registro de entrada e saída de novas placas no estacionamento.  

👁️ **Acesso à Área Secreta**  
- Botão discreto no topo da tela principal.  
- Solicitação de senha administradora.
- Verificação feita via Firestore para conferir se o usuário é administrador.
- Se for, acesso garantido à tela `Admin`. Se não, aparece um `Toast` dizendo que o acesso foi negado.

🎨 **Interface**  
- Componentes estilizados com `CardView`, `EditText`, `Material TextInput`, e botões personalizados.
- Diálogo customizado com senha e botão de confirmação.

---

## 🛠️ Tecnologias Utilizadas

- **Java**
- **Android Studio**
- **Firebase Firestore** (para autenticação e armazenamento dos usuários)
- **ConstraintLayout** e **CardView** para layout
- **SharedPreferences** para manter informações de sessão

---

## 📱Integrantes do grupo

	- Carlos Eduardo Perrud Sousa
	- Marai Júlia Sene Dawla
	- Mateus Mancini Cabrini Araujo
	- Rodrigo Alex Moreira Dos Santos
