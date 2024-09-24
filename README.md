# DnD Monster Generator
This is a simple project for generating homebrewed monsters to be used as inspiration.

## I want to use this.
If you are not interested in writing code for this, you can use the application by starting it using Docker and providing an Open-Ai-Key.
Follow this:
1. Install [Docker](https://www.docker.com/)
2. Get yourself an Open-Ai-Key from [OpenAI](https://platform.openai.com/api-keys). This will need a credit card, but since the Text-Generation used Gpt4-mini, 20 prompts cost like a cent or so, so don't worry. Image-Generation uses Dall-E-3, which costs a little, but you're still ar about 3 cents for an image.
3. Checkout (or copy) the main docker-compose.yml (the one saying `# I'm the main one` at the beginning)
4. Go into the folder with the docker-compose.yml using some kind of shell (cmd, bash, etc) and run `set OPEN_AI_KEY=XXX` (Windows) or `export OPEN_AI_KEY=XXX` (Mac) where `XXX` is your Open-Ai-Key.
5. Run `docker-compose up -d`. This starts the backend, frontend and every needed 3rd party service.
When this is up, you can now reach the application in a browser under `http://localhost:4200`.

## I want to develop this
You can develop the application locally, but you will still need OpenAI-Api-Key. Go to [OpenAI](https://platform.openai.com/api-keys), create a new key and export the value to the `OPEN_AI_KEY` environment variable.
Having that, you can start the spring-boot backend from your IDE.
You can start the frontend from the `/frontend` folder by running `ng serve`.
But since monsters get persisted to a Neo4j database, you'll have to have one running.
You'll also need a Min.io bucket, which you also have to provide, since images get persisted there.
You can start both by running `docker-compose up` in the `scripts` folder. This docker-compose file is a reduced version of the main one, not having the front- and backend.

# Key Features
Mostly I build this application to test working with Spring-AI, so the key features include:
* Interacting with a LLM for text- and image-generation
* Obtaining structured output.
* Using a Vector-DB for RAG (Retrival-Augmented Generation).

## Interacting with an LLM
For interacting with an LLM, I use Spring-AI with a GPT4-mini-Chatclient.
This allows prompting Monster-Descriptions and obtaining generated texts.
For Image-Generation I use a Dall-E-3-ImageClient, allowing generation of images.

## Structured Output
Being one thing that fascinated me the most, I tried adding interpreting the text provided by the ChatClient as a class.
For doing that, I needed a sufficient complex datastructure, so I implemented a DnD Monster.
Using the MonsterController, you can prompt to create a Monster and returned it according to the `Monster.class`structure.
This works very well but comes with some minor problems: Sometimes values are not set. Sometimes strings are not uniform between ChatClient-Class (like one time a modifier is '+0', the other time its 'none'), which can cause some errors. 

## Vector-DB
I added Neo4j to use it as a Vector-DB, since it provides distance-searching using Vector-Indexes.
However I currently only persist generated monsters there, I don't use the Vector-Index so far.
In the future, this would allow implementing an update-process for existing monsters, adding them as "correct" monsters to the DB. 
Using Vector-Indexes would allow to more finely orient the generation on monsters you like. 

# Next Steps
- [x] I want to add data to a simple VectorStore using it for RAG.
- [x] I want to look into structured outputs to return results as structured Objects.
* [x] I want to persist generated data to a Neo4j database.
* [x] I want to add a frontend.
* [ ] I want to use the Neo4j as a VectorStore, using the already persisted (and maybe favorited) entities for RAG.
* [ ] I want to look into FunctionCalling for calling APIs from inside the LLM, maybe implementing a Combat-Simulator to double-check the Challenge-Rating.
* [ ] Maybe I add Artifacts, NPCs, Player-Characters and Places too. We'll see...
* [ ] ???
* [ ] Profit
