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
import axios from '../../api'
import { mapGetters } from 'vuex';
import UserImage from '@/components/common/UserImage.vue';


export default {
    prop: ['opp'],
    
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


    },

    created: function(){
            
            this.userList();
        },
}
</script>

