<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!doctype html>
<html data-framework="vue">
<head>
    <meta charset="utf-8">
    <title>备忘录</title>
    <link rel="stylesheet" href="res/todo.css">
    <style>
    [v-cloak] {
        display: none;
    }
    </style>
    <script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
	<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>  
</head>
<body>
	<% 
		String language = request.getParameter("language");
	%>
    <section class="todoapp">
        <header class="header">
            <input id="wanttodo" class="new-todo" autofocus autocomplete="off" placeholder="输入要做的事情" v-model="newTodo" @keyup.enter="addTodo">
        </header>
        <section class="main" v-show="todos.length" v-cloak>
            <input class="toggle-all" type="checkbox" v-model="allDone">
            <ul class="todo-list">
                <li v-for="todo in filteredTodos" class="todo" :key="todo.id" :class="{ completed: todo.completed, editing: todo == editedTodo }">
                    <div class="view">
                        <input class="toggle" type="checkbox" v-model="todo.completed">
                        <label @dblclick="editTodo(todo)">{{ todo.title }}</label>
                        <button class="destroy" @click="removeTodo(todo)"></button>
                    </div>
                    <input class="edit" type="text" v-model="todo.title" v-todo-focus="todo == editedTodo" @blur="doneEdit(todo)" @keyup.enter="doneEdit(todo)" @keyup.esc="cancelEdit(todo)">
                </li>
            </ul>
        </section>
        <footer class="footer" v-show="todos.length" v-cloak>
            <span class="todo-count">
          <strong>{{ remaining }}</strong> {{ remaining | pluralize }} <span id="Hang">未完成</span>
        </span>
            <ul class="filters" style="padding-left: 50px">
                <li><a href="#/all" :class="{ selected: visibility == 'all' }" id="all">所有</a></li>
                <li><a href="#/active" :class="{ selected: visibility == 'active' }" id="doing">正在做</a></li>
                <li><a href="#/completed" :class="{ selected: visibility == 'completed' }" id="Completed">已完成</a></li>
            </ul>
            <button class="clear-completed" @click="removeCompleted" v-show="todos.length > remaining" id="Eliminate">
               清除已完成的
            </button>
        </footer>
    </section>
    <footer class="info">
        <p id="Prompt">回车输入事件，双击编辑事件</p>
    </footer>
   
    </script>
    <script src="https://cdn.bootcss.com/vue/2.0.0-rc.5/vue.min.js"></script>
    <script>
    
    $(document).ready(function(){
		jQuery.i18n.properties({
            name : 'strings', //资源文件名称
            path : '/scp/common/i18n/', //资源文件路径
            mode : 'map', //用Map的方式使用资源文件中的值
            language : '<%=language%>',
            callback : function() {//加载成功后设置显示内容
                $('#Prompt').html($.i18n.prop('回车输入事件，双击编辑事件'));
                $('#all').html($.i18n.prop('所有'));
                $('#doing').html($.i18n.prop('正在做'));
                $('#Completed').html($.i18n.prop('已完成'));
                $('#Eliminate').html($.i18n.prop('清除已完成的'));
                $('#wanttodo').attr('placeholder',$.i18n.prop('输入要做的事情'));
                $('#Hang').html($.i18n.prop('未完成'));
            }
        });
	});
	
    // 本地存储
    var STORAGE_KEY = 'todos-vuejs-2.0'
    var todoStorage = {
        fetch: function() {
            var todos = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]')
            todos.forEach(function(todo, index) {
                todo.id = index
            })
            todoStorage.uid = todos.length
            return todos
        },
        save: function(todos) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(todos))
        }
    }
    // filters组件
    var filters = {
        all: function(todos) {
            return todos
        },
        active: function(todos) {
            return todos.filter(function(todo) {
                return !todo.completed
            })
        },
        completed: function(todos) {
            return todos.filter(function(todo) {
                return todo.completed
            })
        }
    }
    var app = new Vue({
        data: {
            todos: todoStorage.fetch(),
            newTodo: '',
            editedTodo: null,
            visibility: 'all'
        },
        watch: {
            todos: {
                handler: function(todos) {
                    todoStorage.save(todos)
                },
                deep: true
            }
        },
        computed: {
            filteredTodos: function() {
                return filters[this.visibility](this.todos)
            },
            remaining: function() {
                return filters.active(this.todos).length
            },
            allDone: {
                get: function() {
                    return this.remaining === 0
                },
                set: function(value) {
                    this.todos.forEach(function(todo) {
                        todo.completed = value
                    })
                }
            }
        },
        filters: {
            pluralize: function(n) {
            	if('<%=language%>'=='en'){
            		return 'term';
            	}else{
                	return '项'
                }
            }
        },
        methods: {
            addTodo: function() {
                var value = this.newTodo && this.newTodo.trim()
                if (!value) {
                    return
                }
                this.todos.push({
                    id: todoStorage.uid++,
                    title: value,
                    completed: false
                })
                this.newTodo = ''
            },
            removeTodo: function(todo) {
                this.todos.splice(this.todos.indexOf(todo), 1)
            },
            editTodo: function(todo) {
                this.beforeEditCache = todo.title
                this.editedTodo = todo
            },
            doneEdit: function(todo) {
                if (!this.editedTodo) {
                    return
                }
                this.editedTodo = null
                todo.title = todo.title.trim()
                if (!todo.title) {
                    this.removeTodo(todo)
                }
            },
            cancelEdit: function(todo) {
                this.editedTodo = null
                todo.title = this.beforeEditCache
            },
            removeCompleted: function() {
                this.todos = filters.active(this.todos)
            }
        },
        directives: {
            'todo-focus': function(el, value) {
                if (value) {
                    el.focus()
                }
            }
        }
    })
    // 处理路由
    function onHashChange() {
        var visibility = window.location.hash.replace(/#\/?/, '')
        if (filters[visibility]) {
            app.visibility = visibility
        } else {
            window.location.hash = ''
            app.visibility = 'all'
        }
    }
    window.addEventListener('hashchange', onHashChange)
    onHashChange()
    app.$mount('.todoapp')
    </script>
</body>
</html>
