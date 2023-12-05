export interface PostLinks {
	post: {
		id: Number,
		content: String,
		person: String,
		name: String
	},
	links: {
		[key: string]: string
	}
}
