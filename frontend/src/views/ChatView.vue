<template>
<div class="container clearfix">
    
    <!-- <LeftSide></LeftSide> -->

    <div class="people-list" id="people-list">
        <div class="search">
            <label for="me">My Account : {{this.getUid}}</label>
        </div>
        <ul class="list" id="usersList" >
            <a href="#" @click="selectUser(i.id, i.name)" :key="i.id" :value="i" v-for="i in userData">
                <li class="clearfix" >
                    <UserImage v-bind:uid="i.id"></UserImage>
                    <div class="about">
                        <div id="userNameAppender" class="name">{{i.name}}</div>
                        <div class="status">
                            <i class="fa fa-circle offline"></i>
                        </div>
                    </div>
                </li>
            </a>
        </ul>
    </div>
    
    

    <div class="chat">
        <div class="chat-header clearfix">
            <img alt="avatar" height="55px"
                 src="https://source.unsplash.com/featured/face=3" width="55px"/>

            <div class="chat-about">
                <div class="chat-with" id="selectedUserId" v-text="opponent">{{opponent}}</div>
                <button v-on:click="openModal">Report</button>

                <MyModal v-if="showModal">
                    <h1 slot="header" class="modal__header">Report Page</h1>
                    <hr>
                    <div slot="body" class="modal__body">
                        <div class="report-page">
                            <form v-on:submit="submitForm">
                                <p v-if="errors.length">
                                    <b>Please correct the following error(s):</b>
                                    <ul>
                                        <li v-for="error in errors" :key="error"> {{ error }}</li>
                                    </ul>
                                </p>

                                <div>
                                    <label for="name">My Account : {{this.getUid}}</label> 
                                </div>
                                <div>
                                    <label> {{this.reported}}</label>
                                </div>
                                <br>
                               
                                <div id='list-of-issue'>
                                    <h1 slot="header" class="modal__sub_header">신고하는 이유를 선택해주세요</h1>
                                    <hr>
                                    <br>
                                    <div>
                                        <label for="issue1">판매금지 품목이에요</label>
                                        <input type="radio" id="issue1" value="issue1" v-model="picked">
                                    </div>
                                    <div>
                                        <label for="issue2">중고거래 게시글이 아니에요</label>
                                        <input type="radio" id="issue2" value="issue2" v-model="picked">
                                    </div>
                                    <div>
                                        <label for="issue3">전문 판매업자 같아요</label>
                                        <input type="radio" id="issue3" value="issue3" v-model="picked">
                                    </div>
                                    <div>
                                        <label for="issue4">카카오톡/안전결제 등으로 유도하는 사기글이에요</label>
                                        <input type="radio" id="issue4" value="issue4" v-model="picked">
                                    </div>
                                    <div>
                                        <label for="issue5">기타 사유 선택</label>
                                        <input type="radio" id="issue5" value="issue5" v-model="picked">
                                    </div>
                                    <br>
                                    <span>체크한 항목: {{ picked }}</span>
                                </div>
                                <div>
                                    <span>자세한 사항</span>
                                </div>
                                <div>
                                    <textarea v-model="reportDetail" placeholder="여러줄을 입력해보세요"></textarea>
                                </div>

                                
                                <button type="submit">Submit</button>

                                <!-- <p>
                                    <input type="submit" value="Submit">
                                </p> -->
                            </form>
                        </div>
                    </div>
                    <span slot="footer">
                        
                        <button v-on:click="closeModal">Close</button>
                    </span>
                </MyModal>

                <div class="chat-num-messages"></div>
            </div>
            <i class="fa fa-star"></i>
        </div> <!-- end chat-header -->

        <div class="chat-history">
            <ul>                	
                 <li class="clearfix" id="chat-contents" v-for="(content, index) in array" :key="index">
                    
                    <div v-if="getUid != content.senderId">
                        <div class="message-data">
                        <span class="message-data-name"><i class="fa fa-circle online"></i> {{content.senderId}}</span>
                        <span class="message-data-time">{{content.createDate}}, Today</span>
                        </div>
                        <div class="message my-message">
                            {{content.content}}
                        </div>
                    </div>

                    <div v-else>
                        <div class="message-data align-right">
                        <span class="message-data-time">{{content.createDate}}, Today</span> &nbsp; &nbsp;
                        <span class="message-data-name">You</span> <i class="fa fa-circle me"></i>
                        </div>
                        <div class="message other-message float-right">
                            {{content.content}}
                        </div>
                    </div>

                    
                </li>
                
            </ul>

        </div> <!-- end chat-history -->

        <div class="chat-message clearfix">
            <textarea id="message-to-send" name="message-to-send" placeholder="Type your message" rows="3" v-model="message"></textarea>

            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>
            
            <button id="sendBtn" @click="sendMessage(message)">Send</button>

            
        </div> <!-- end chat-message -->

    </div> <!-- end chat -->

</div> <!-- end container -->
</template>

<style src="../assets/css/chat.css"></style>



<script>
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

import axios from '../api'
import { mapGetters } from 'vuex';
import UserImage from '@/components/common/UserImage.vue';
import MyModal from '@/components/common/CommonModal.vue';
//import LeftSide from '@/components/common/LeftSide.vue';


let socket = new SockJS('http://localhost:8081/fleamarket/ws');
let stompClient = Stomp.over(socket);
export default {
    name: 'App',
    
    components: {
        UserImage,
        MyModal,
        //leftSide,
        
    },

    computed: {
        ...mapGetters(['getUid']),
        
    },

    data: function() {
        return {

        userData : [       ],
        url: 'http://localhost:8081/fleamarket',
        data : null,
        selectedUser : '',
        opponent : '',
        reported : '',
        message : '',       
        array : [],
        time : null,
        

        showModal: false,   // 모달의 상태를 관리하는 변수

        errors: [],
        name: null,
        age: null,
        movie: null,
        picked : '',
        reportDetail: '',

        }
    },
    methods: {
            
            openModal() {   // 모달의 상태 변수를 `true`로 변경한다.
            this.showModal = true;
            },
            closeModal() {  // 모달의 상태 변수를 `false`로 변경한다.
            this.showModal = false;
            },

            checkForm: function (e) {
                if (this.name && this.age) {
                    return true;
                }

                this.errors = [];

                if (!this.name) {
                    this.errors.push('Name required.');
                }
                if (!this.age) {
                    this.errors.push('Age required.');
                }

                e.preventDefault();
                },

            submitForm(){
             
                axios.post('/report', {
                    reportId : this.getUid,
                    reportedId : this.selectedUser,
                    reportType : this.picked,
                    reportDetail : this.reportDetail
                })
                .then(res => {
                    console.log(res);
                }).catch(err => {
                    console.log(err);
                })
            },

            // 유저 리스트 불러오는 함수
            userList(){
              
              axios.get("/chat")
                
                .then(response => {
                    
                    console.log(response.data);

                    let userList = response.data;

                    this.userData = userList;

                    console.log("??" + this.userData);
                })
                .catch(function(error){
                    console.log(error);
                }) 

            },
           
            connectToChat(userId) {

                console.log("connecting to chat..." + userId);
                console.log("URL : " + this.url + '/ws');
                               
                stompClient.connect({}, (frame) => {

                    console.log("connected to: " + frame);

                    stompClient.subscribe("/topic/messages/" + userId, (response) => {

                        let data = JSON.parse(response.body);

                        console.log("DB에 저장된 시간" + data.createDate);

                        data.createDate = new Date(data.createDate).toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");

                        console.log("DB에 저장된 시간(변경)" + data.createDate);

                        console.log(data);
                        this.array.push(data);

                        this.scrollToBottom();
                               
                    });
                });
            },

            // 채팅 기록 불러오는 함수
            recallChat(selectedUser) {
               
                // axios.post('/recallChat', null, {params: {
                //     sender : this.getUid,
                //     receiver : selectedUser,
                // }})
                axios.get(`/recallChat/${this.getUid}/${selectedUser}` )

                .then(response => {
                    
                   let messages = response;
                        
                    console.log(messages);
                })
                .catch(function(error){
                    console.log(error);
                })  
            },

            selectUser(userId, userName) {

                console.log("selecting users: " + userId);
                this.selectedUser = userId;
                console.log(userName);
                
                //유저 이동시 스크린에 채팅 지우기
                //$('li').remove("#chat-contents");
                
                this.recallChat(this.selectedUser);
                            
                this.opponent ='Chat with ' + userName;
                this.reported ='Would you like to report ' + userName +'?';
            
            },

            getCurrentTime() {
                console.log(new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3"));
                return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
            },
        
            sendMsg(from, text) {
                stompClient.send(`/app/chat/${from}/${this.selectedUser}`, {}, JSON.stringify({
                    senderId: from,
                    receiverId: this.selectedUser,
                    content: text,
                                        
                }));
            },

            //Vue 방식으로 바꿀수있으면 바꿔야함
            scrollToBottom() {

                window.$chatHistory = window.$('.chat-history');
                window.$chatHistory.scrollTop(window.$chatHistory[0].scrollHeight);
                
            },
            
            sendMessage(message) {
    
                this.sendMsg(this.getUid, message);
                this.scrollToBottom();    
                this.scrollToBottom();
                this.message = '';
                    
                }
            },
          
                        
        created: function(){
            this.connectToChat(this.getUid);
            this.userList();
        },
            
}
</script>

<style>
.modal__header {
	font-weight: 700;
	font-size: 24px;
}

.modal__sub_header {
    font-weight: 500;
    font-size: 18px;
}

.modal__body {
	width: 500px;
    height: 700px;
    margin-top: 30px;
}
</style>




