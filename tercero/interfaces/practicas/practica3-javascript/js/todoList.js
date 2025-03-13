const buttonAddTask = document.getElementById('addTaskButton');
const inputField = document.getElementById('taskInput');

const handleAddTask = () => {
	const taskName = inputField.value;
	if (taskName !== '') {
		const ul = document.getElementById('todoList');
		const li = document.createElement('li');
		li.className = 'list-group-item d-flex flex-row align-items-center';
		const span = document.createElement('span');
		span.className = 'taskName flex-grow-1';
		span.innerHTML = taskName;
		const imgUp = document.createElement('img');
		imgUp.addEventListener('click', handleMoveTaskUp);
		imgUp.src = 'images/taskUp.png';
		const imgDown = document.createElement('img');
		imgDown.addEventListener('click', handleMoveTaskDown);
		imgDown.src = 'images/taskDown.png';
		const imgRemove = document.createElement('img');
		imgRemove.addEventListener('click', handleRemoveTask);
		imgRemove.src = 'images/removeTask.png';
		li.appendChild(span);
		li.appendChild(imgUp);
		li.appendChild(imgDown);
		li.appendChild(imgRemove);
		ul.appendChild(li);
		inputField.value = '';
	}

}

const handleRemoveTask = (event) => {
	event.target.parentElement.remove();
}

const handleMoveTaskUp = (event) => {
	const li = event.target.parentElement;
	const prevLi = li.previousElementSibling;
	if (prevLi) {
		li.parentElement.insertBefore(li, prevLi);
	}
}

const handleMoveTaskDown = (event) => {
	const li = event.target.parentElement;
	const nextLi = li.nextElementSibling;
	if (nextLi) {
		li.parentElement.insertBefore(nextLi, li);
	}
}


buttonAddTask.addEventListener('click', handleAddTask);
inputField.addEventListener('keypress', (event) => {
	if (event.key === 'Enter') {
		handleAddTask();
	}
})
