<template>
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
</template>
<script>
import { mapGetters } from 'vuex';
import UserImage from '@/components/common/UserImage.vue';
import {eventBus}  from '@/main.js';
import chatApi from '@/api/chat.js';

export default {
    
    components: {
        UserImage,     
    },

    computed: {
        ...mapGetters(['getUid']),
    },

    data: function() {
        return {
            userData : [       ],
            selectedUser : '',
            opponent : '',
            reported : '',
        }
    },
    methods: {
        // 유저 리스트 불러오는 함수
            userList(){

            chatApi.bringUpChatUser()
              
                .then(response => {
                    
                    console.log(response.data);

                    let userList = response.data;

                    this.userData = userList;

                })
                .catch(function(error){
                    console.log(error);
                }); 

            },

            // 채팅 기록 불러오는 함수
            recallChat(getUid,selectedUser) {
               
                chatApi.bringUpMessageRecord(getUid, selectedUser)

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
                
                this.recallChat(this.getUid, this.selectedUser);
                            
                this.opponent ='Chat with ' + userName;
                this.reported ='Would you like to report ' + userName +'?';
        
                eventBus.userIsSelected(this.selectedUser)

                eventBus.opntIsSelected(this.opponent)

                eventBus.reportIsSelected(this.reported)
                
            },


    },

    created: function(){
            
            this.userList();
        },
}
</script>

