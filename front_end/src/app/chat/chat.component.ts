import { Component, OnInit } from '@angular/core';
import {Client} from '@stomp/stompjs';
import *  as SockJs from 'sockjs-client';
import {Mensagem} from './models/mensagem';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  private client : Client;
  conectado:boolean = false;

  mensagem:Mensagem = new Mensagem();
  mensagens :Mensagem[] = [];
  escrevendo:string;
  clienteId: string;

  constructor() {
      this.clienteId = 'id-' + new Date().getUTCMilliseconds()+'-'+Math.random().toString(36).substr(2);
  }

  ngOnInit(){

    this.client = new Client();
    this.client.webSocketFactory = () =>{
      return new SockJs("http://localhost:8080/chat-websocket");
    }

    this.client.onConnect = (frame) => {
        console.log('conectado');
        console.log(frame);
        this.conectado = true;

        this.client.subscribe('/chat/mensagem', e =>  {
          let mensagem : Mensagem = JSON.parse(e.body) as Mensagem;
          mensagem.data = new Date(mensagem.data);


          if(!this.mensagem.cor && this.mensagem.tipo=='Novo_Usuario' && mensagem.usuario == this.mensagem.usuario){
            this.mensagem.cor = mensagem.cor;

            console.log(this.mensagem)
          }
          this.mensagens.push(mensagem);
        })

        this.client.subscribe('/chat/escrevendo', e =>  {
          this.escrevendo = e.body;
          setTimeout(() => {
            this.escrevendo ='';
          }, 3000);
        });

        this.client.subscribe('/chat/historico/'+this.clienteId, e =>  {
            const historicos = JSON.parse(e.body) as Mensagem[];
            this.mensagens = historicos.map(m => {
                m.data = new Date(m.data);
              return m;
            }).reverse();
        });

        this.client.publish({destination: '/app/historico', body:this.clienteId});

        this.mensagem.tipo='Novo_Usuario';
        this.client.publish({destination:'/app/mensagem', body: JSON.stringify(this.mensagem)});
    }

    this.client.onDisconnect = (frame) => {
        console.log('desconectado');
        console.log(frame);
        this.conectado = false;
        this.mensagem = new Mensagem();
        this.mensagens = [];
    }
  }

  conectar():void{
    this.client.activate();
  }

  desconectar():void{
    this.client.deactivate();

  }

  enviarMensagem():void{
    this.mensagem.tipo='Mensagem';

    this.client.publish({destination:'/app/mensagem', body: JSON.stringify(this.mensagem)});
    this.mensagem.texto = '';
  }

  escrevendoMensagem():void{
    this.client.publish({destination:'/app/escrevendo', body: this.mensagem.usuario});

  }

}
